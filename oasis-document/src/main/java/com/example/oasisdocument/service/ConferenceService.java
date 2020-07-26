package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.extendDoc.Conference;

import java.util.List;

public interface ConferenceService {
	Conference fetchEnById(String id);

	JSONObject fetchConferenceList(int pageNum, int pageSize,String rankKey);

	JSONArray fetchConferenceList(String refinement, int pageNum, int pageSize);

}
