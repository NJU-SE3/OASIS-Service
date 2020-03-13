package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.docs.nodes.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository("AuthorRepositoryNeo")
public interface AuthorRepository extends Neo4jRepository<Author, Long> {
}
