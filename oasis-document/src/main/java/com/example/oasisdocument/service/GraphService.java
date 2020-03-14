package com.example.oasisdocument.service;

import org.springframework.scheduling.annotation.Async;

public interface GraphService {
	void constructGraph();

	@Async
	void importAuthorBasic();
}
