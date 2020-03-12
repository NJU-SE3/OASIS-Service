package com.example.oasisdocument.docs;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
public class Author extends BaseEntity {
	private String authorName;
	private String affiliationName;
	private String coAuthors;
	private int articleCount;
	private String paperTrends;
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

	public String getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(String coAuthors) {
		this.coAuthors = coAuthors;
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public String getPaperTrends() {
		return paperTrends;
	}

	public void setPaperTrends(String paperTrends) {
		this.paperTrends = paperTrends;
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
