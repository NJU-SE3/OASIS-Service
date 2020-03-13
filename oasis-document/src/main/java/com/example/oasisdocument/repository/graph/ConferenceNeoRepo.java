package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.model.graph.nodes.Conference;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ConferenceNeoRepo extends Neo4jRepository<Conference, Long> {
}
