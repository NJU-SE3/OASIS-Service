package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "conference")
public class ConferenceNeo extends BaseNeo {
	private String conferenceName;

	@Relationship(type = "published_on", direction = Relationship.INCOMING)
	@JSONField(serialize = false, deserialize = false)
	private Set<PaperNeo> paperNeoSet;

	public ConferenceNeo() {
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public Set<PaperNeo> getPaperNeoSet() {
		return paperNeoSet;
	}

	public void setPaperNeoSet(Set<PaperNeo> paperNeoSet) {
		this.paperNeoSet = paperNeoSet;
	}
}
