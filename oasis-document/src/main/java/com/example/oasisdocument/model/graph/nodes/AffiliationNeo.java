package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "affiliation")
public class AffiliationNeo extends BaseNeo {
	private String affiliationName;

	@Relationship(type = "work_in", direction = Relationship.INCOMING)
	@JSONField(serialize = false, deserialize = false)
	private Set<AuthorNeo> authorNeoSet;

	public AffiliationNeo() {
	}

	public String getAffiliationName() {
		return affiliationName;
	}

	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}

	public Set<AuthorNeo> getAuthorNeoSet() {
		return authorNeoSet;
	}

	public void setAuthorNeoSet(Set<AuthorNeo> authorNeoSet) {
		this.authorNeoSet = authorNeoSet;
	}
}
