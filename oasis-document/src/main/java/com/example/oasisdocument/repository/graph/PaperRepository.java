package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.docs.nodes.Paper;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository("PaperRepositoryNeo")
public interface PaperRepository extends Neo4jRepository<Paper, Long> {
}
