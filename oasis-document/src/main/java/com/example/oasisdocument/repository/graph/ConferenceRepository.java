package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.docs.nodes.Conference;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository("ConferenceRepositoryNeo")
public interface ConferenceRepository extends Neo4jRepository<Conference, Long> {
}
