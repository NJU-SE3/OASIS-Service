package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AuthorService {
	void insert(Author entity);

	JSONObject fetchEnById(String id);

	JSONObject fetchAuthorList(int pageNum, int pageSize,String rankKey);

	JSONArray fetchAuthorList(String refinement, int pageNum, int pageSize);

	JSONArray fetchAuthorSummaryUponField();

	JSONArray fetchAuthorWithRefine(List<String> fieldNames,int pageNum, int pageSize);
}
