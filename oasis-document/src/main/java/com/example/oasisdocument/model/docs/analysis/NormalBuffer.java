package com.example.oasisdocument.model.docs.analysis;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NormalBuffers")
public class NormalBuffer extends BaseDoc {
	private String type;
	private String content;

	public NormalBuffer(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
