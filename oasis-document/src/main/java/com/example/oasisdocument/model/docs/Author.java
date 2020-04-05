package com.example.oasisdocument.model.docs;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

	public static Set<String> getAllFields(Author entity) {
		final String splitter = ",";
		String[] spl = entity.field.split(splitter);
		Set<String> ans = new HashSet<>();
		for (String s : spl){
			if (!s.isEmpty()) ans.add(s);
		}
		return ans;
	}

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

}
