package com.example.oasispaper.model.VO;

public class AuthorVO extends BaseVO {
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
