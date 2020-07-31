package com.example.oasisdocument.options;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Field;

import java.io.Serializable;

/**
 * 用户关注度传输实体
 */
public class AttentionDTO implements Serializable {
    private String fieldName;        //领域
    private Author author;      //作者
    private int year;           //年份
    private double score;       //得分


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
