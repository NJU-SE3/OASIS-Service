package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.docs.Paper;

import java.util.List;

public interface PaperService {
    List<PaperBriefVO> queryPaper(String key, String returnFacets);

    List<PaperBriefVO> queryPaperRefine(List<PaperBriefVO> papers, List<String> refinements);

    void insert(PaperInsertVO entity);

    JSONObject papersSummary(List<PaperBriefVO> papers);
}
