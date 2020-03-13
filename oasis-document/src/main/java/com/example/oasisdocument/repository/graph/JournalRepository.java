package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.docs.nodes.Journal;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository("JournalRepositoryNeo")
public interface JournalRepository extends Neo4jRepository<Journal, Long> {
}
