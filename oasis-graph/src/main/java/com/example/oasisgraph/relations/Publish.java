package com.example.oasisgraph.relations;

import com.example.oasisgraph.nodes.Author;
import com.example.oasisgraph.nodes.Paper;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity("PUBLISH")
public class Publish {
	@Id
	@GeneratedValue
	private Long id;

	@StartNode
	private Author author;
	@EndNode
	private Paper paper;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}
}
