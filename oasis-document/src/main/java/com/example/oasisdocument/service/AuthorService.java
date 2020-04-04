package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;

public interface AuthorService {
	void insert(Author entity);

	JSONObject fetchEnById(String id);

	JSONArray fetchAuthorList(int pageNum, int pageSize);

	JSONArray fetchAuthorList(String refinement, int pageNum, int pageSize);

}
