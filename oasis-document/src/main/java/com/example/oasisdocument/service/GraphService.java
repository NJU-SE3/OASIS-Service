package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;

public interface GraphService {

	JSONObject centeralAuthor(String authorId);

	JSONObject fieldMapViaId(String fieldId);

	JSONObject affMapViaId(String affId);
}
