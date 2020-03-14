package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.AuthorNodeVO;
import com.example.oasisdocument.model.docs.analysis.NormalBuffer;
import com.example.oasisdocument.model.graph.nodes.Affiliation;
import com.example.oasisdocument.model.graph.nodes.Author;
import com.example.oasisdocument.repository.analysis.NormalBufferRepo;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.repository.graph.AffiliationNeoRepo;
import com.example.oasisdocument.repository.graph.AuthorNeoRepo;
import com.example.oasisdocument.repository.graph.PaperNeoRepo;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GraphServiceImpl implements GraphService {
	private static final String authorBufName = "author_ana";

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private AuthorNeoRepo authorNeoRepo;

	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private PaperNeoRepo paperNeoRepo;

	@Autowired
	private NormalBufferRepo normalBufferRepo;


	@Autowired
	private AffiliationNeoRepo affiliationNeoRepo;

	/**
	 * TODO:
	 * 1. author.csv 中的数据进行导入. terms , trends , coAuthor 除外.
	 * affiliation 字段的数据, 需要额外存储
	 * 2. paper.csv 数据导入. references,citations 除外
	 */
	@Override
	public void constructGraph() {

	}

	@Override
	public void importAuthorBasic() {
		for (com.example.oasisdocument.model.docs.Author vo : authorRepository.findAll()) {
			String affName = vo.getAffiliationName();
			//insert into aff
			Author author = AuthorNodeVO.VO2PO(vo);

			//save to affiliation
			if (!affName.isEmpty()) {
				List<Affiliation> affList = affiliationNeoRepo.findAllByAffiliationName(affName);
				Affiliation affEntity = affList.isEmpty() ? new Affiliation() : affList.get(0);
				affEntity.addAuthor(author);
				affiliationNeoRepo.save(affEntity);
			}
			//save to author
			authorNeoRepo.save(author);
			//save to buffer
			long id = vo.getId().longValue();
			NormalBuffer buffer = new NormalBuffer();
			buffer.setId(String.valueOf(id));
			buffer.setType(authorBufName);
			JSONObject parser = new JSONObject();
			parser.put("coAuthors", vo.getCoAuthors());
			parser.put("trends", vo.getField());
			parser.put("terms", vo.getTerms());
			//序列化
			buffer.setContent(parser.toJSONString());
			//save to buf
			normalBufferRepo.save(buffer);
		}
	}
}
