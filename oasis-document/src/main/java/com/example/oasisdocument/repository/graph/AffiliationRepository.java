package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.docs.nodes.Affiliation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "AffiliationRepositoryNeo")
public interface AffiliationRepository extends Neo4jRepository<Affiliation, Long> {
}
