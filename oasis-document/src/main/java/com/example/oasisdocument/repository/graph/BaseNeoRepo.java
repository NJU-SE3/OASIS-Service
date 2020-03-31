package com.example.oasisdocument.repository.graph;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseNeoRepo<T> extends Neo4jRepository<T, String> {
	T findByXid(String xid);
}
