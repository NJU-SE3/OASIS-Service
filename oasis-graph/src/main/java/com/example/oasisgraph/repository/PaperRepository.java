package com.example.oasisgraph.repository;

import com.example.oasisgraph.nodes.Paper;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PaperRepository extends Neo4jRepository<Paper, Long> {
}
