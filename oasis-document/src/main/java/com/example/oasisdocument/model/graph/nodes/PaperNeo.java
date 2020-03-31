package com.example.oasisdocument.model.graph.nodes;


import com.alibaba.fastjson.annotation.JSONField;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "paper")
public class PaperNeo extends BaseNeo {
	private String title;

	@Relationship(type = "publish", direction = Relationship.INCOMING)
	@JSONField(serialize = false)
	private Set<AuthorNeo> authorNeoSet;

	public PaperNeo() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<AuthorNeo> getAuthorNeoSet() {
		return authorNeoSet;
	}

	public void setAuthorNeoSet(Set<AuthorNeo> authorNeoSet) {
		this.authorNeoSet = authorNeoSet;
	}
}
