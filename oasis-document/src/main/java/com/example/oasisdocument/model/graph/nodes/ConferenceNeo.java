package com.example.oasisdocument.model.graph.nodes;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "conference")
public class ConferenceNeo extends BaseNeo {
	private String conferenceName;

	public ConferenceNeo() {
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}
}
