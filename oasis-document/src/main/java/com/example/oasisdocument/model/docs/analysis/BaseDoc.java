package com.example.oasisdocument.model.docs.analysis;

import org.springframework.data.annotation.Id;

import javax.persistence.ExcludeSuperclassListeners;
import java.io.Serializable;

@ExcludeSuperclassListeners
public class BaseDoc implements Serializable {
	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
