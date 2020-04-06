package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.PaperInsertVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PaperService {

	JSONObject queryPaper(final String key, final String returnFacets,
								 int pageSize, int pageNum,
								 HttpServletRequest request,
								 HttpServletResponse response);

	JSONObject queryPaperRefine(String qid, List<String> refinements,
							   int pageNum, int pageSize, HttpServletRequest request);

	void insertPaperVOEntity(PaperInsertVO entity);

	JSONObject papersSummary( String qid,
							  HttpServletRequest request);

	List<PaperBriefVO> fetchPaperList(String id);

}
