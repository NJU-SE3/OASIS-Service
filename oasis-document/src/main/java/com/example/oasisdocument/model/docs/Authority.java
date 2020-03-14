package com.example.oasisdocument.model.docs;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authority")
public class Authority extends BaseEntity {
    private String authorityName;       //权限

    public Authority(){}

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
