package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import jdk.nashorn.internal.objects.annotations.Property;

import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.Id;
import java.io.Serializable;

@ExcludeSuperclassListeners
public class BaseNeo implements Serializable {
	@Id
	@JSONField(serialize = false)
	private Long id;

	@Id
	@Property(name = "ID")
	@JSONField(name = "id")
	private String ID;


	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
