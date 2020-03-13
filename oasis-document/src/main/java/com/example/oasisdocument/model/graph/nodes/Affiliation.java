package com.example.oasisdocument.model.graph.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@NodeEntity
public class Affiliation implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Relationship(type = "affiliation_own", direction = Relationship.OUTGOING)
	private List<Author> authorList;

	public void addAuthor(Author author) {
		if (null == authorList) authorList = new LinkedList<>();
		authorList.add(author);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Author> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
}
