package com.example.oasisdocument.repository.graph;

import com.example.oasisdocument.model.graph.nodes.Paper;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PaperNeoRepo extends Neo4jRepository<Paper, Long> {
}
