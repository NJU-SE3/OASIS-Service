package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.model.graph.nodes.Affiliation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AffiliationNeoRepo extends Neo4jRepository<Affiliation, Long> {
}
