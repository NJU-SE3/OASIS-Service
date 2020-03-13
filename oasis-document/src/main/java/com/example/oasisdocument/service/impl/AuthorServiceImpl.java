package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorRepository authorRepository;

	@Override
	public void insert(Author entity) {
		if(authorRepository.findAllById(entity.getId()).isEmpty())
			authorRepository.save(entity);
	}
}
