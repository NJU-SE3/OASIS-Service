package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author_conference")
public class AuthorConference extends BaseEntity {
    @Column(name = "author_id")
    private int authorId;

    @Column(name = "conference_id")
    private int conferenceId;

    @Column(name = "paper_id")
    private int paperId;

    @Column(name = "start_page")
    private int startPage = 0;

    @Column(name = "end_page")
    private int endPage = 0;

    @Column(name = "pdf_link")
    private String link = "";

    @Column(name = "year")
    private int year = 0;

    public AuthorConference() {
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
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
