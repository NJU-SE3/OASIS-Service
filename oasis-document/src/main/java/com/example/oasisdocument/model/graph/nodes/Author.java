package com.example.oasisdocument.model.graph.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者实体
 */

@NodeEntity
public class Author implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Relationship(type = "author_publish", direction = Relationship.OUTGOING)
	private List<Paper> papers;

	@Relationship(type = "author_belong_affiliation", direction = Relationship.INCOMING)
	private Affiliation affiliation;

	public void publish(Paper paper) {
		if (papers == null) this.papers = new LinkedList<>();
		this.papers.add(paper);
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}
}
