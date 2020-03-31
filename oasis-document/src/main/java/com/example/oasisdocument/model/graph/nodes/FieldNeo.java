package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.oasisdocument.model.docs.Paper;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity(label = "field")
public class FieldNeo extends BaseNeo {
	private String fieldName;

	@Relationship(type = "describe", direction = Relationship.INCOMING)
	@JSONField(serialize = false, deserialize = false)
	private Set<PaperNeo> paperNeoSet;

	FieldNeo(){}

	public Set<PaperNeo> getPaperNeoSet() {
		return paperNeoSet;
	}

	public void setPaperNeoSet(Set<PaperNeo> paperNeoSet) {
		this.paperNeoSet = paperNeoSet;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
