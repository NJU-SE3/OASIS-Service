package com.example.oasisgraph.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.List;

@NodeEntity
public class Paper implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String title;

	@Relationship(type = "published_by_author", direction = Relationship.INCOMING)
	private List<Author> authors;

	@Relationship(type = "published_via_journal", direction = Relationship.INCOMING)
	private Journal journal;

	@Relationship(type = "published_via_conference", direction = Relationship.INCOMING)
	private Conference conference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}
}
