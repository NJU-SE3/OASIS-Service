package com.example.oasisdocument.model.docs.extendDoc;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "affiliations")
public class Affiliation extends BaseEntity {
	private String affiliationName;                //机构名称

	public Affiliation() {
	}

	public String getAffiliationName() {
		return affiliationName;
	}

	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}

}
