package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private InitializationService initializationService;

	@Override
	public void insert(Author entity) {
		if (authorRepository.findAllById(entity.getId()).isEmpty())
			authorRepository.save(entity);
	}

	@Override
	public Author fetchEnById(String id) {
		List<Author> list = authorRepository.findAllById(id);
		if (list.isEmpty()) throw new EntityNotFoundException();
		return list.get(0);
	}

	@Override
	public List<Author> fetchAuthorList() {
		return mongoTemplate.findAll(Author.class);
	}

	//查找某一个限定条件下的作者列表
	@Override
	public List<Author> fetchAuthorList(String affiliationId) {
		CounterBaseEntity en = initializationService.getSummaryInfo(affiliationId);
		List<Author> authors = new LinkedList<>();
		en.getPaperList().forEach((String aid) -> authors.addAll(authorRepository.findAllById(aid)));
		return authors;
	}

}
