package com.example.oasisgraph.repository;

import com.example.oasisgraph.nodes.Journal;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface JournalRepository extends Neo4jRepository<Journal, Long> {
}
