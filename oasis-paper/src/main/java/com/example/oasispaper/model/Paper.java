package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "paper")
public class Paper extends BaseEntity {
    @Column
    private String title;       //论文名

    @Column(name = "abstract")
    private String abstra;      //摘要

    @Column(name = "keywords")
    private String keywords;    //关键词及

    @Column(name = "terms")     //术语
    private String terms;

    @Column(name = "citation_count")
    private int citationCount;

    @Column(name = "reference_count")
    private int referenceCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
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
}
