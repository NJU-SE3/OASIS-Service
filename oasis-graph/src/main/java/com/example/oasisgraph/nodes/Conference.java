package com.example.oasisgraph.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@NodeEntity
public class Conference implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@Relationship(type = "conference_publish_paper")
	private List<Paper> paperList;

	private int year;            //会议年

	private String conferenceName;

	public void publish(Paper paper) {
		if (null == this.paperList) this.paperList = new LinkedList<>();
		this.paperList.add(paper);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Paper> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Paper> paperList) {
		this.paperList = paperList;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}
}
