package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;

import java.math.BigInteger;

public interface AuthorService {
	void insert(Author entity);

	Author fetchEnById(String id);
}
