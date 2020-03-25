package com.example.oasisdocument.repository.docs;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.repository.BaseMongoRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface AuthorRepository extends BaseMongoRepo<Author> {
	List<Author> findAllById(String id);

	List<Author> findAllByAuthorName(String authorName);
}
