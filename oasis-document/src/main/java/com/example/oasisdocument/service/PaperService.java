package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.PaperInsertVO;

import java.util.List;

public interface PaperService {

	JSONArray queryPaper(String key, String returnFacets);

	JSONArray queryPaperRefine(JSONArray papers, List<String> refinements);

	void insertPaperVOEntity(PaperInsertVO entity);

	JSONObject papersSummary(JSONArray papers);

	List<PaperBriefVO> fetchPaperList(String id);

}
