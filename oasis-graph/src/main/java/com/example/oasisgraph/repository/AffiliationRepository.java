package com.example.oasisgraph.repository;

import com.example.oasisgraph.nodes.Affiliation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AffiliationRepository extends Neo4jRepository<Affiliation, Long> {
}
