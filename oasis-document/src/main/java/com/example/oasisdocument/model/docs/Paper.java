package com.example.oasisdocument.model.docs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "papers")
public class Paper extends BaseEntity {
    @Indexed
    private String title;       //paper名.

    @Field("abstract")
    @JSONField(name = "abstract")
    private String abstra;      //摘要

    @Field("conference")
    @Indexed
    private String conference;  //会议名

    @Field("terms")
    private String terms;       //术语

    @Field("keywords")
    @Indexed
    private String keywords;    //论文关键字

    @Field("pdfLink")
    private String pdfLink;     //pdf

    @Field("citationCount")
    private int citationCount;  //citation count

    @Field("referenceCount")
    private int referenceCount;

    @Field("year")
    private int year;           //发布年

    @Field("authors")
    @Indexed
    @JSONField(name = "authors")
    private String authors;     //作者名

    @Field("affiliations")
    @Indexed
    @JSONField(name = "affiliations")
    private String affiliations; //机构

    @DBRef(lazy = true)
    private List<Author> authorList;
    @DBRef(lazy=true)
    private Conference conferenceEntity;

    @Field("citations")
    private List<Long> citations;
    @Field("references")
    private List<Long> references;

    public Paper() {
    }

    public static Set<String> getAllTerms(Paper entity) {
        Set<String> ans = new HashSet<>();
        JSONObject object = new JSONObject();
        try {
            object = (JSONObject) JSON.parse(entity.terms);
        } catch (Exception e) {
            entity.terms = "";
        }
        for (String key : object.keySet()) {
            JSONArray array = object.getJSONArray(key);
            List<String> termList = JSON.parseArray(array.toJSONString(), String.class);
            ans.addAll(termList);
        }
        return ans;
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


    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
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

    public Conference getConferenceEntity() {
        return conferenceEntity;
    }

    public void setConferenceEntity(Conference conferenceEntity) {
        this.conferenceEntity = conferenceEntity;
    }
}
