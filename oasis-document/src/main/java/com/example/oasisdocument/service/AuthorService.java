package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;

import java.util.List;

public interface AuthorService {
	void insert(Author entity);

	Author fetchEnById(String id);

	List<Author> fetchAuthorList(int pageNum, int pageSize);

	List<Author> fetchAuthorList(String refinement);

}
