package com.example.oasisgraph.VO;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

//插入paper时的完整paper VO
public class PaperVO extends BaseVO {
	private String title;       //paper名.

	@JSONField(name = "abstract")
	private String abstra;      //摘要

	private String conference;  //会议名

	private String terms;       //术语

	private String keywords;    //论文关键字

	private String startPage;      //起始页

	private String endPage;        //结束页

	private String pdfLink;     //pdf

	private int citationCount;  //citation count

	private int referenceCount;

	private int year;           //发布年

	@JSONField(name = "authors")
	private List<String> authors;     //作者名

	@JSONField(name = "affiliations")
	private List<String> affiliations; //机构

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
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

	public int getReferenceCount() {
		return referenceCount;
	}

	public void setReferenceCount(int referenceCount) {
		this.referenceCount = referenceCount;
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

	public String getAbstra() {
		return abstra;
	}

	public void setAbstra(String abstra) {
		this.abstra = abstra;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(int citationCount) {
		this.citationCount = citationCount;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<String> getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(List<String> affiliations) {
		this.affiliations = affiliations;
	}
}
