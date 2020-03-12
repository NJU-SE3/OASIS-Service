package com.example.oasisdocument.docs;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
public class Author extends BaseEntity {
	private String authorName;
	private String affiliationName;
	private String colAuthors;
	private int articleCount;
	private String tends;
	private String bioParagraphs;
	private String field;
	private String terms;
	private String photoUrl;

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAffiliationName() {
		return affiliationName;
	}

	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}

	public String getColAuthors() {
		return colAuthors;
	}

	public void setColAuthors(String colAuthors) {
		this.colAuthors = colAuthors;
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public String getTends() {
		return tends;
	}

	public void setTends(String tends) {
		this.tends = tends;
	}

	public String getBioParagraphs() {
		return bioParagraphs;
	}

	public void setBioParagraphs(String bioParagraphs) {
		this.bioParagraphs = bioParagraphs;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
