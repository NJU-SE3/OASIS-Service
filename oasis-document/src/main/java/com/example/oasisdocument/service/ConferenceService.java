package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.model.docs.extendDoc.Conference;

import java.util.List;

public interface ConferenceService {
	Conference fetchEnById(String id);

	JSONArray fetchConferenceList(int pageNum, int pageSize);

	JSONArray fetchConferenceList(String refinement, int pageNum, int pageSize);

}
