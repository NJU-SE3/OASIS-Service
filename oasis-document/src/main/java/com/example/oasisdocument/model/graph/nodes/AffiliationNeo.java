package com.example.oasisdocument.model.graph.nodes;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "affiliation")
public class AffiliationNeo extends BaseNeo {
	private String affiliationName;


	public AffiliationNeo() {
	}

	public String getAffiliationName() {
		return affiliationName;
	}

	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}
}
