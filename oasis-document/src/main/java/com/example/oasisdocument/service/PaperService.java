package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.docs.Paper;

import java.util.List;

public interface PaperService {
    List<Paper> queryPaper(String key, String returnFacets);

    List<Paper> queryPaperRefine(List<Paper> papers, List<String> refinements);

    void insert(PaperInsertVO entity);

    JSONObject papersSummary(List<Paper> papers);
}
