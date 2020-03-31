package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;

import java.util.List;
import java.util.Set;

public interface GraphService {

	JSONObject centeralAuthor(String authorId);
}
