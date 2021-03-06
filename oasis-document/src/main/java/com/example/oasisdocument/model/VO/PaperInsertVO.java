package com.example.oasisdocument.model.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.oasisdocument.model.docs.BaseEntity;
import com.example.oasisdocument.model.docs.Paper;

import java.util.List;

public class PaperInsertVO extends BaseEntity {
	private String title;       //paper名.

	@JSONField(name = "abstract")
	private String abstra;      //摘要

	private String conference;  //会议名

	private String terms;       //术语

	private String keywords;    //论文关键字

	private String pdfLink;     //pdf

	private int citationCount;  //citation count

	private int referenceCount;

	private int year;           //发布年

	@JSONField(name = "authors")
	private String authors;     //作者名

	@JSONField(name = "affiliations")
	private String affiliations; //机构

	private List<Long> authorIds;

	private List<Long> citations;
	private List<Long> references;

	public PaperInsertVO() {
	}


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

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}

	public List<Long> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<Long> authorIds) {
		this.authorIds = authorIds;
	}

	public Paper VO2PO() {
		Paper paper = new Paper();
		paper.setId(this.getId());
		paper.setTitle(this.title);
		paper.setAbstra(this.abstra);
		paper.setConference(this.conference);
		paper.setTerms(this.terms);
		paper.setKeywords(this.keywords);
		paper.setPdfLink(this.pdfLink);
		paper.setCitationCount(this.citationCount);
		paper.setReferenceCount(this.referenceCount);
		paper.setYear(this.year);
		paper.setAuthors(this.authors);
		paper.setAffiliations(this.affiliations);
		paper.setReferences(this.references);
		paper.setCitations(this.citations);
		return paper;
	}

	public List<Long> getCitations() {
		return citations;
	}

	public void setCitations(List<Long> citations) {
		this.citations = citations;
	}

	public List<Long> getReferences() {
		return references;
	}

	public void setReferences(List<Long> references) {
		this.references = references;
	}
}
