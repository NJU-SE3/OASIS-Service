package com.example.oasisdocument.model.graph.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者实体
 */
//id,preferredName,firstName,lastName,affiliation,coAuthors,articleCount,trends,bioParagraphs,publicTopic,terms,photoUrl
@NodeEntity
public class Author implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	private String authorName;

	private int articleCount;        //发表论文数量

	private String bioParagraphs;    //简介

	private String publicTopic;        //关键词

	private String photoUrl;        //avatar

	//Relations
	@Relationship(type = "author_publish", direction = Relationship.OUTGOING)
	private List<Paper> papers;

	public void publish(Paper paper) {
		if (papers == null) this.papers = new LinkedList<>();
		this.papers.add(paper);
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
