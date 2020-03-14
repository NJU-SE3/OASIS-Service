package com.example.oasisdocument.model.VO;

//id,preferredName,firstName,lastName,affiliation,coAuthors,articleCount,trends,bioParagraphs,publicTopic,terms,photoUrl

import com.example.oasisdocument.model.graph.nodes.Author;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.io.Serializable;
import java.util.List;

public class AuthorNodeVO implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	private String authorName;

	private int articleCount;        //发表论文数量

	private String bioParagraphs;    //简介

	private String publicTopic;        //关键词

	private String photoUrl;        //avatar

	private String affiliation;        //机构名称

	//分析型数据
	private List<Long> coAuthors;

	private String field;

	private String terms;

	public static Author VO2PO(AuthorNodeVO vo) {
		Author ans = new Author();
		ans.setId(vo.id);
		ans.setAuthorName(vo.authorName);
		ans.setArticleCount(vo.articleCount);
		ans.setBioParagraphs(vo.bioParagraphs);
		ans.setPublicTopic(vo.publicTopic);
		ans.setPhotoUrl(vo.photoUrl);
		return ans;
	}

	public AuthorNodeVO() {
	}

	public static Author VO2PO(com.example.oasisdocument.model.docs.Author vo) {
		Author ans = new Author();
		ans.setId(vo.getId().longValue());
		ans.setAuthorName(vo.getAuthorName());
		ans.setArticleCount(vo.getArticleCount());
		ans.setBioParagraphs(vo.getBioParagraphs());
		ans.setPublicTopic(vo.getAffiliationName());
		ans.setPhotoUrl(vo.getPhotoUrl());
		return ans;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public String getBioParagraphs() {
		return bioParagraphs;
	}

	public void setBioParagraphs(String bioParagraphs) {
		this.bioParagraphs = bioParagraphs;
	}

	public String getPublicTopic() {
		return publicTopic;
	}

	public void setPublicTopic(String publicTopic) {
		this.publicTopic = publicTopic;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public List<Long> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(List<Long> coAuthors) {
		this.coAuthors = coAuthors;
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
}
