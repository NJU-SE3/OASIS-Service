package com.example.oasisdocument.model.graph.nodes;

import com.alibaba.fastjson.annotation.JSONField;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.List;

@NodeEntity
public class Paper implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	private String title;        //标题

	@JSONField(name = "abstract")
	private String abstr;        //摘要
	private int year;            //年份
	private String pdfLink;     //pdf


	//作者,由作者引入边
	@Relationship(type = "published_by_author", direction = Relationship.INCOMING)
	private List<Author> authors;

	//期刊
	@Relationship(type = "published_via_journal", direction = Relationship.INCOMING)
	private Journal journal;

	//会议
	@Relationship(type = "published_via_conference", direction = Relationship.INCOMING)
	private Conference conference;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPdfLink() {
		return pdfLink;
	}

	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}
}
