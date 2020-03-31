package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.graph.nodes.AffiliationNeo;
import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.model.graph.nodes.FieldNeo;
import com.example.oasisdocument.model.graph.nodes.PaperNeo;
import com.example.oasisdocument.repository.graph.*;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
	private GeneralJsonVO generalJsonVO;

	@Override
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
		JSONArray edges = new JSONArray(), nodes = new JSONArray();
		// construct the nodes set
		nodes.add(generalJsonVO.authorNeo2VO(centerEntity));
		nodes.addAll(neighbors.stream()
				.map((AuthorNeo neo) -> generalJsonVO.authorNeo2VO(neo)).collect(Collectors.toList()));
		// construct the edges set
		for (AuthorNeo authorNeo : neighbors) {
			JSONObject obj = new JSONObject();
			obj.put("begin", centerEntity.getXid());
			obj.put("end", authorNeo.getXid());
			edges.add(obj);
		}
		JSONObject ans = new JSONObject();
		ans.put("nodes", nodes);
		ans.put("edges", edges);

		return ans;
	}

	@Override
	public JSONObject fieldMapViaId(String fieldId) {
		FieldNeo centerEntity = fieldNeoRepo.findByXid(fieldId);
		if (null == centerEntity) throw new BadReqException();
		// One neighbour
		Set<FieldNeo> neighbors = new HashSet<>();
		for (PaperNeo paper : centerEntity.getPaperNeoSet()) {
			paper = paperNeoRepo.findByXid(paper.getXid());
			for (FieldNeo neo : paper.getFieldNeoSet()) {
				if (neo.getXid().equals(centerEntity.getXid())) continue;
				neighbors.add(neo);
			}
		}
		JSONArray edges = new JSONArray(), nodes = new JSONArray();
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
		ans.put("nodes", nodes);
		ans.put("edges", edges);

		return ans;
	}

	@Override
	public JSONObject affMapViaId(String affId) {
		AffiliationNeo centerEntity = affiliationNeoRepo.findByXid(affId);
		if (null == centerEntity) throw new BadReqException();
		// One neighbour
		Set<FieldNeo> neighbors = new HashSet<>();
		return new JSONObject();
	}
}
