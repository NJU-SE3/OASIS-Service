package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.extendDoc.Field;


public interface FieldService {
	Field fetchEnById(String id);

	JSONArray fetchFieldList(String refinement, int pageNum, int pageSize);

	JSONObject fetchFieldList(int pageNum, int pageSize, String rankKey);

	void insertFields(Paper entity);

	JSONArray fetchFieldDistribution(String id);

    JSONArray fetchFieldByName(String fieldName, int pageNum, int pageSize);
}
