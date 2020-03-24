package com.example.oasisdocument.model.docs.extendDoc;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "affiliations")
public class Affiliation extends BaseEntity {
	private String affiliationName;                //机构名称
	@DBRef(lazy = true)
	private List<Author> authorList;

	public Affiliation() {
	}

	public String getAffiliationName() {
		return affiliationName;
	}

	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}

	public List<Author> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
}
