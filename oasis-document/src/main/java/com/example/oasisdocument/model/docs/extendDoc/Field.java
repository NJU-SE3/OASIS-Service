package com.example.oasisdocument.model.docs.extendDoc;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "fields")
public class Field extends BaseEntity {
	private String fieldName;            //领域名称
	@DBRef(lazy = true)
	private List<Author> authorList;

	public Field() {
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
