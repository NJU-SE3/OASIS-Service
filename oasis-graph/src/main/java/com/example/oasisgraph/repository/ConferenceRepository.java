package com.example.oasisgraph.repository;

import com.example.oasisgraph.nodes.Conference;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ConferenceRepository extends Neo4jRepository<Conference, Long> {
}
