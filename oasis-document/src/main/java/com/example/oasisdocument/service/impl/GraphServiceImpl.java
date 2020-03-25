package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.service.GraphService;
import com.example.oasisdocument.service.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GraphServiceImpl implements GraphService {
	private static final String authorSplitter = ";";
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private InitializationService initializationService;
	@Autowired
	private PaperRepository paperRepository;

	//作者关系图构建
	@Override
	public List<Set<GraphEdge>> authorMapViaId(String id) {
		//首先查找图中是否已经存在这个顶点
		List<GraphEdge> edges = mongoTemplate.find(Query.query(new Criteria("begin").is(id)), GraphEdge.class);
		if (!edges.isEmpty()) return new LinkedList<>();
		//如果不存在 , 那么进行持久化
		List<Author> authors = authorRepository.findAllById(id);
		if (authors.isEmpty()) throw new EntityNotFoundException();
		Author author = authors.get(0);
		assert null != author;
		String beginId = author.getId();
		//获取协同作者列表
		List<Paper> papers = new LinkedList<>();
		initializationService.getSummaryInfo(id)
				.getPaperList().forEach((String pid) -> papers.addAll(paperRepository.findAllById(pid)));
		//对于每一篇paper
		List<Set<GraphEdge>> graph = new LinkedList<>();
		for (Paper paper : papers) {
			graph.add(authorMap(author, paper));
		}
		return graph;
	}

	/**
	 * 针对一个paper记录 , 添加多个边
	 *
	 * @param begin : 起始作者顶点
	 * @param paper : paper信息
	 */
	private Set<GraphEdge> authorMap(Author begin, Paper paper) {
		Set<GraphEdge> curSet = new HashSet<>();
		for (Author end : paper.getAuthorList()) {
			GraphEdge edge = new GraphEdge();
			edge.setBegin(begin.getId());
			edge.setEnd(end.getId());
		}
		return curSet;
	}

	//领域图构建
	@Override
	public List<GraphEdge> fieldMapViaId(String fieldId) {
		//首先查找图中是否已经存在这个顶点
		List<GraphEdge> edges = mongoTemplate.find(Query.query(new Criteria("begin").is(fieldId)),
				GraphEdge.class);
		//如果已经存在了 , 那么直接返回
		if (!edges.isEmpty()) return edges;
		//如果不存在 , 那么进行持久化
		//首先找到中心点 Field
		Field beginNode = mongoTemplate.findById(fieldId, Field.class);
		if (null == beginNode) throw new EntityNotFoundException();
		//接下来查询中心节点关联的所有paper
		CounterBaseEntity baseEntity =
				mongoTemplate.findOne(Query.query(new Criteria("checkId").is(fieldId)),
						CounterBaseEntity.class);
		if (null == baseEntity) throw new EntityNotFoundException();
		List<String> paperIds = baseEntity.getPaperList();
		//得到邻接点集合
		// map from field id to related terms counter
		Map<String, Integer> neighbors = new HashMap<>();
		for (String paperId : paperIds) {
			Paper paper = mongoTemplate.findById(paperId, Paper.class);
			assert null != paper;
			for (String authorName : paper.getAuthors().split(authorSplitter)) {
				Author author = mongoTemplate.findOne(Query.query(new Criteria("authorName").is(authorName))
						, Author.class);
				if (author == null) continue;
				for (String fieldName : author.getField().split(",")) {
					Field en = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(fieldName)),
							Field.class);
					if (en != null && author.getTerms().contains(beginNode.getFieldName())) {
						neighbors.put(en.getId(), neighbors.getOrDefault(en.getId(), 0) + 1);
					}
				}
			}
		}
		List<GraphEdge> ans = new LinkedList<>();
		//对于每一个邻接点 , 进行weight计算
		for (String nodeId : neighbors.keySet()) {
			GraphEdge edge = new GraphEdge();
			edge.setBegin(beginNode.getId());
			edge.setEnd(nodeId);
			edge.setWeight(neighbors.get(nodeId));
			ans.add(edge);
			//执行持久化
//			mongoTemplate.save(edge);
		}
		return ans;
	}

	//机构图构建
	@Override
	public List<GraphEdge> affMapViaId(String id) {
		return null;
	}
}
