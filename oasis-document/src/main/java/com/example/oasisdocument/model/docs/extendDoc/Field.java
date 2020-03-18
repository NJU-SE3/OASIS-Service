package com.example.oasisdocument.model.docs.extendDoc;

import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fields")
public class Field extends BaseEntity {
	private String fieldName;            //领域名称

	public Field() {
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
