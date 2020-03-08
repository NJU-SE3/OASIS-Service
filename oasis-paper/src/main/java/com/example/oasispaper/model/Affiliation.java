package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "affiliation")
public class Affiliation extends BaseEntity {
    @Column(name = "affiliation_name")
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
