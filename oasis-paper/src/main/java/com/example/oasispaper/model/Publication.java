package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//发布实体
@Entity
@Table(name = "publication")
public class Publication extends BaseEntity {
	@Column(name = "author_id")
	private Long authorId;

	@Column(name = "conference_id")
	private Long conferenceId;

	@Column(name = "paper_id")
	private Long paperId;

	@Column(name = "start_page")
	private String startPage = "";

	@Column(name = "end_page")
	private String endPage = "";

	@Column(name = "pdf_link")
	private String link = "";

	@Column(name = "year")
	private int year = 0;

	public Publication() {
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(Long conferenceId) {
		this.conferenceId = conferenceId;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getEndPage() {
		return endPage;
	}

	public void setEndPage(String endPage) {
		this.endPage = endPage;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
