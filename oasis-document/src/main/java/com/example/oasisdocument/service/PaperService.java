package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.docs.Paper;

import java.util.List;

public interface PaperService {
    List<Paper> queryPaper(String query, int pageNum, int pageSize);

    void insert(Paper entity);
}
