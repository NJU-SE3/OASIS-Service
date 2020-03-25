package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.service.impl.PaperServiceImpl;

import java.util.List;

public interface PaperService {
	enum EntityType {
		AFFILIATION, AUTHOR, FIELD, CONFERENCE
	}

	List<PaperBriefVO> queryPaper(String key, String returnFacets);

	List<PaperBriefVO> queryPaperRefine(List<PaperBriefVO> papers, List<String> refinements);

	void insertPaperVOEntity(PaperInsertVO entity);

	JSONObject papersSummary(List<PaperBriefVO> papers);

	List<Paper> fetchPaperList(String id);

	//根据 其他各种实体 id 获取隶属
	List<Paper> fetchPaperList(String id, PaperServiceImpl.EntityType type);
}
