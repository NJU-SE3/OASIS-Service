package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author")
public class Author extends BaseEntity {
    @Column(name = "affiliation_id")
    private Long affiliationId;

    @Column(name = "author_name")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(Long affiliationId) {
        this.affiliationId = affiliationId;
    }
}
