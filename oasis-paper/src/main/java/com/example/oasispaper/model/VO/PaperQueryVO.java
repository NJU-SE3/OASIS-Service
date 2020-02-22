package com.example.oasispaper.model.VO;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

//paper查询VO
public class PaperQueryVO extends BaseVO {

    private String title;       //paper名

    @JSONField(name = "abstract")
    private String abstra;      //摘要

    private String conference;  //会议名

    private String terms;       //术语

    private String keywords;    //论文关键字

    private List<AuthorInner> authors;  //作者列表

    public static class AuthorInner {

        private String name;     //作者名
        private String affiliation; //机构
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAffiliation() {
            return affiliation;
        }

        public void setAffiliation(String affiliation) {
            this.affiliation = affiliation;
        }

    }
    public PaperQueryVO() {
    }

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

    public List<AuthorInner> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorInner> authors) {
        this.authors = authors;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
