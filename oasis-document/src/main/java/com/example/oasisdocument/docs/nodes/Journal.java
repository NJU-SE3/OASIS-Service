package com.example.oasisdocument.docs.nodes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.LinkedList;
import java.util.List;

@NodeEntity
public class Journal {
	@Id
	@GeneratedValue
	private Long id;

	@Relationship(type = "journal_publish_paper")
	private List<Paper> paperList;

	public void publish(Paper paper){
		if (null == this.paperList) this.paperList = new LinkedList<>();
		this.paperList.add(paper);
	}

	private String journalName;

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

	public String getJournalName() {
		return journalName;
	}

	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}
}
