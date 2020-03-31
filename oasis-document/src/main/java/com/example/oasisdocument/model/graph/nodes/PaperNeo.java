package com.example.oasisdocument.model.graph.nodes;


import com.alibaba.fastjson.annotation.JSONField;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "paper")
public class PaperNeo extends BaseNeo {
	private String title;

	@Relationship(type = "publish", direction = Relationship.INCOMING)
	@JSONField(serialize = false, deserialize = false)
	private Set<AuthorNeo> authorNeoSet;

	@Relationship(type = "published_on", direction = Relationship.OUTGOING)
	@JSONField(serialize = false, deserialize = false)
	private Conference conference;

	@Relationship(type = "describe", direction = Relationship.OUTGOING)
	@JSONField(serialize = false, deserialize = false)
	private Set<FieldNeo> fieldNeoSet;

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

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public Set<FieldNeo> getFieldNeoSet() {
		return fieldNeoSet;
	}

	public void setFieldNeoSet(Set<FieldNeo> fieldNeoSet) {
		this.fieldNeoSet = fieldNeoSet;
	}
}
