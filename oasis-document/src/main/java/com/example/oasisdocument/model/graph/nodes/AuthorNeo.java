package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.Set;

@NodeEntity(label = "author")
public class AuthorNeo extends BaseNeo {
	private String authorName;

	@Relationship(type = "publish", direction = Relationship.OUTGOING)
	@JSONField(serialize = false)
	private Set<PaperNeo> paperNeoSet;

	public AuthorNeo() {
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Set<PaperNeo> getPaperNeoSet() {
		return paperNeoSet;
	}

	public void setPaperNeoSet(Set<PaperNeo> paperNeoSet) {
		this.paperNeoSet = paperNeoSet;
	}
}
