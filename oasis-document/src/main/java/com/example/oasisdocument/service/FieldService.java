package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.extendDoc.Field;

import java.util.List;


public interface FieldService {
	Field fetchEnById(String id);

	JSONArray fetchFieldList(String refinement, int pageNum, int pageSize);

	JSONArray fetchFieldList(int pageNum, int pageSize);

	void insertFields(Paper entity);

	JSONArray fetchFieldDistribution(String id);
}
