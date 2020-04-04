package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;

import java.util.List;


public interface FieldService {
	Field fetchEnById(String id);

	List<Field> fetchFieldList(String refinement);

	List<Field> fetchFieldList(int pageNum, int pageSize);

	void insertFields(Paper entity);

	JSONArray fetchFieldDistribution(String id);
}
