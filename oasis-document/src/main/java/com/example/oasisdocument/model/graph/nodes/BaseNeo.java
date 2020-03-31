package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import jdk.nashorn.internal.objects.annotations.Property;

import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.Id;
import java.io.Serializable;

@ExcludeSuperclassListeners
public class BaseNeo implements Serializable {
	@Id
	@JSONField(serialize = false, deserialize = false)
	private Long id;

	@Id
	@Property(name = "xid")
	@JSONField(name = "id")
	private String xid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}
}
