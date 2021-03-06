package com.example.oasisdocument.model.docs;

import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;

@ExcludeSuperclassListeners
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1529685098267757690L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
