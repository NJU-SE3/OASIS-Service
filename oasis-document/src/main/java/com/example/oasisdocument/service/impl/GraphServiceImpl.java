package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.repository.graph.AuthorNeoRepo;
import com.example.oasisdocument.repository.graph.PaperNeoRepo;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class GraphServiceImpl implements GraphService {
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private AuthorNeoRepo authorNeoRepo;

	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private PaperNeoRepo paperNeoRepo;

	@Override
	@Async
	public void constructGraph() {

	}
}
