package com.example.oasispaper.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "conference")
public class Conference extends BaseEntity {
    @Column
    private String name;    //会议名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
