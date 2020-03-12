package com.example.oasisdocument.repository;

import com.example.oasisdocument.docs.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, BigInteger> {
	List<Author> findAllById(BigInteger id);
}
