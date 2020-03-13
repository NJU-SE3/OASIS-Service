package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.model.graph.nodes.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AuthorNeoRepo extends Neo4jRepository<Author, Long> {
}
