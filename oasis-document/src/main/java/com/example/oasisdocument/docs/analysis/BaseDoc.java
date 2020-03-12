package com.example.oasisdocument.docs.analysis;

import org.springframework.data.annotation.Id;

import javax.persistence.ExcludeSuperclassListeners;
import java.io.Serializable;

@ExcludeSuperclassListeners
public class BaseDoc implements Serializable {
	@Id
	private String id;

	private String body;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
