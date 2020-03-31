package com.example.oasisdocument.model.docs;

import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Document(collection = "authors")
public class Author extends BaseEntity {
	@Indexed
	private String authorName;
	@Field("affiliationName")
	@Indexed
	private String affiliationName;
	private String coAuthors;           //协同作者 , 为后续分析使用
	private int articleCount;
	private String paperTrends;        //趋势
	private String bioParagraphs;
	private String field;            //领域summary
	private String terms;
	private String photoUrl;

	@DBRef(lazy = true)
	private Affiliation affiliationEntity;
	@DBRef(lazy = true)
	private List<Paper> paperList;

	public Author() {
	}

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

	public Affiliation getAffiliationEntity() {
		return affiliationEntity;
	}

	public void setAffiliationEntity(Affiliation affiliationEntity) {
		this.affiliationEntity = affiliationEntity;
	}

	public List<Paper> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Paper> paperList) {
		this.paperList = paperList;
	}
}
