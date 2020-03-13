package com.example.oasisdocument.repository.docs;

import com.example.oasisdocument.model.docs.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, BigInteger> {
	List<Author> findAllById(BigInteger id);
}
