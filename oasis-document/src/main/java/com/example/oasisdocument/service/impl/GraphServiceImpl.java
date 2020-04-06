package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.graph.nodes.AffiliationNeo;
import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.model.graph.nodes.FieldNeo;
import com.example.oasisdocument.model.graph.nodes.PaperNeo;
import com.example.oasisdocument.repository.graph.*;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class GraphServiceImpl implements GraphService {
	@Autowired
	private AuthorNeoRepo authorNeoRepo;
	@Autowired
	private PaperNeoRepo paperNeoRepo;
	@Autowired
	private AffiliationNeoRepo affiliationNeoRepo;
	@Autowired
	private ConferenceNeoRepo conferenceNeoRepo;
	@Autowired
	private FieldNeoRepo fieldNeoRepo;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CounterService counterService;
	@Autowired
	private GeneralJsonVO generalJsonVO;

	@Override
	@Cacheable(cacheNames = "centeralAuthor", unless = "#result==null")
	public JSONObject centeralAuthor(String authorId) {
		AuthorNeo centerEntity = authorNeoRepo.findByXid(authorId);
		if (null == centerEntity) throw new EntityNotFoundException();
		// 寻找一度关系作者
		Set<AuthorNeo> neighbors = new HashSet<>();

		for (PaperNeo paper : centerEntity.getPaperNeoSet()) {
			paper = paperNeoRepo.findByXid(paper.getXid());
			for (AuthorNeo neo : paper.getAuthorNeoSet()) {
				if (neo.getXid().equals(centerEntity.getXid())) continue;
				neighbors.add(neo);
			}
		}
		JSONArray edges = new JSONArray();
		List<AuthorNeo> nodes = new LinkedList<>();
		// construct the nodes set
		nodes.add(centerEntity);
		nodes.addAll(neighbors);
		// construct the edges set
		for (AuthorNeo authorNeo : neighbors) {
			JSONObject obj = new JSONObject();
			obj.put("begin", centerEntity.getXid());
			obj.put("end", authorNeo.getXid());
			edges.add(obj);
		}
		JSONObject ans = new JSONObject();
		ans.put("nodes", nodes.stream()
				.map((AuthorNeo neo) -> appendActiveness(generalJsonVO.authorNeo2VO(neo)))
				.collect(Collectors.toList())
		);
		ans.put("edges", edges);

		return ans;
	}

	@Override
	@Cacheable(cacheNames = "fieldMapViaId", unless = "#result==null")
	public JSONObject fieldMapViaId(String fieldId) {
		FieldNeo centerEntity = fieldNeoRepo.findByXid(fieldId);
		if (null == centerEntity) throw new EntityNotFoundException();
		// One neighbour
		Set<FieldNeo> neighbors = new HashSet<>();
		for (PaperNeo paper : centerEntity.getPaperNeoSet()) {
			paper = paperNeoRepo.findByXid(paper.getXid());
			for (FieldNeo neo : paper.getFieldNeoSet()) {
				if (neo.getXid().equals(centerEntity.getXid())) continue;
				neighbors.add(neo);
			}
		}
		JSONArray edges = new JSONArray();
		List<FieldNeo> nodes = new LinkedList<>();
		// construct the nodes set
		nodes.add(centerEntity);
		nodes.addAll(neighbors);
		// construct the edges set
		for (FieldNeo nei : neighbors) {
			JSONObject obj = new JSONObject();
			obj.put("begin", centerEntity.getXid());
			obj.put("end", nei.getXid());
			edges.add(obj);
		}
		JSONObject ans = new JSONObject();
		ans.put("nodes", nodes.stream()
				.map((FieldNeo neo) -> appendActiveness(generalJsonVO.fieldNeo2VO(neo)))
				.collect(Collectors.toList()));
		ans.put("edges", edges);

		return ans;
	}

	@Override
	@Cacheable(cacheNames = "affMapViaId", unless = "#result==null")
	public JSONObject affMapViaId(String affId) {
		AffiliationNeo centerEntity = affiliationNeoRepo.findByXid(affId);
		if (null == centerEntity) throw new EntityNotFoundException();
		Set<AffiliationNeo> neighbors = new HashSet<>();
		for (AuthorNeo authorNeo : centerEntity.getAuthorNeoSet()) {
			//Search into author
			authorNeo = authorNeoRepo.findByXid(authorNeo.getXid());
			Set<AuthorNeo> relatedAuthors = getCoAuthors(authorNeo);
			for (AuthorNeo relatedAuthor : relatedAuthors) {
				relatedAuthor = authorNeoRepo.findByXid(relatedAuthor.getXid());
				if (null != relatedAuthor
						&& null != relatedAuthor.getAffiliationNeo()
						&& !relatedAuthor.getAffiliationNeo().getXid().equals(affId))
					neighbors.add(relatedAuthor.getAffiliationNeo());
			}
		}
		JSONArray edges = new JSONArray();
		List<AffiliationNeo> nodes = new LinkedList<>();
		nodes.add(centerEntity);
		nodes.addAll(neighbors);
		for (AffiliationNeo affiliationNeo : neighbors) {
			JSONObject obj = new JSONObject();
			obj.put("begin", affId);
			obj.put("end", affiliationNeo.getXid());
			edges.add(obj);
		}
		JSONObject ans = new JSONObject();

		ans.put("nodes", nodes.stream()
				.map((AffiliationNeo affiliationNeo) -> appendActiveness(generalJsonVO.affNeo2VO(affiliationNeo)))
				.collect(Collectors.toList()));
		ans.put("edges", edges);
		return ans;
	}

	private JSONObject appendActiveness(JSONObject obj) {
		String id = obj.getString("id");
		CounterBaseEntity en = counterService.getSummaryInfo(id);
		obj.put("activeness", en.getActiveness());
		return obj;
	}

	private Set<AuthorNeo> getCoAuthors(AuthorNeo center) {
		Set<AuthorNeo> neighbors = new HashSet<>();
		if (center.getPaperNeoSet() == null) return neighbors;
		for (PaperNeo paper : center.getPaperNeoSet()) {
			paper = paperNeoRepo.findByXid(paper.getXid());
			for (AuthorNeo neo : paper.getAuthorNeoSet()) {
				if (neo.getXid().equals(center.getXid())) continue;
				neighbors.add(neo);
			}
		}
		return neighbors;
	}
}
