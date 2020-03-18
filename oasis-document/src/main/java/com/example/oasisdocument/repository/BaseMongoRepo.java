package com.example.oasisdocument.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseMongoRepo<T> extends MongoRepository<T, String> {
}
