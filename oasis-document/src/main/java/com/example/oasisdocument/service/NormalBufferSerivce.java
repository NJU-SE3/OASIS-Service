package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;

public interface NormalBufferSerivce {
	void storeAuthorFieldSummary();

	JSONArray loadAuthorFieldSummary();
}
