package com.example.oasisdocument.model.VO;

import com.example.oasisdocument.model.docs.Paper;

import java.io.Serializable;

public class PaperBriefVO implements Serializable {
	private String id;
	private String title;
	private String conference;  //会议名
	private String terms;       //术语
	private String keywords;    //论文关键字
	private String pdfLink;     //pdf
	private int citationCount;  //citation count
	private int referenceCount;
	private int year;           //发布年
	private String authors;     //作者名
	private String affiliations; //机构


	public static PaperBriefVO PO2VO(Paper paper) {
		PaperBriefVO vo = new PaperBriefVO();
		vo.id = paper.getId();
		vo.title = paper.getTitle();
		vo.conference = paper.getConference();
		vo.terms = String.join(",", Paper.getAllTerms(paper));
		vo.keywords = paper.getKeywords();
		vo.pdfLink = paper.getPdfLink();
		vo.citationCount = paper.getCitationCount();
		vo.referenceCount = paper.getReferenceCount();
		vo.year = paper.getYear();
		vo.authors = paper.getAuthors();
		vo.affiliations = paper.getAffiliations();
		return vo;
	}

	public PaperBriefVO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPdfLink() {
		return pdfLink;
	}

	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}

	public int getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(int citationCount) {
		this.citationCount = citationCount;
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
}
