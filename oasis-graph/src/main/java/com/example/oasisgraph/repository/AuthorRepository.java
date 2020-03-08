package com.example.oasisgraph.repository;

import com.example.oasisgraph.nodes.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AuthorRepository extends Neo4jRepository<Author, Long> {
}
