package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Service
public class GraphServiceImpl implements GraphService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private InitializationService initializationService;
	@Autowired
	private PaperRepository paperRepository;

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

	@Override
	public List<GraphEdge> fieldMapViaId(String id) {
		//首先查找图中是否已经存在这个顶点
		List<GraphEdge> edges = mongoTemplate.find(Query.query(new Criteria("begin").is(id)), GraphEdge.class);
		if (!edges.isEmpty()) return edges;
		//如果不存在 , 那么进行持久化
		List<Field> fields = mongoTemplate.find(Query.query(new Criteria("id").is(id)), Field.class);
		if (fields.isEmpty()) throw new EntityNotFoundException();
		Field field = fields.get(0);
		assert null != field;
		String beginId = field.getId();
		List<Paper> papers = mongoTemplate
				.find(Query.query(new Criteria("keywords").regex(field.getFieldName())), Paper.class);
		for (Paper paper : papers) {
			for (String coName : paper.getKeywords().split(";")) {
				
			}

		}
		return null;
	}

	@Override
	public List<GraphEdge> affMapViaId(String id) {
		return null;
	}
}
