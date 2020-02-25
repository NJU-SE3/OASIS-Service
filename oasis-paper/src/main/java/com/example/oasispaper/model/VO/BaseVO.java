package com.example.oasispaper.model.VO;

import java.io.Serializable;

public class BaseVO implements Serializable {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
