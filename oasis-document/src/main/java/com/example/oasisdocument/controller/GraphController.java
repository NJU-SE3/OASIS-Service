package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/graph")
public class GraphController {
	@Autowired
	private GraphService graphService;
	@Autowired
	private GeneralJsonVO generalJsonVO;

	@GetMapping("/author")
	public JSONObject authorMapViaId(@RequestParam(name = "id") String authorId) {
		return graphService.centeralAuthor(authorId);
	}

	@GetMapping("/field")
	public JSONArray fieldMapViaId(@RequestParam(name = "id") String fieldId) {
		JSONArray array = new JSONArray();
		return array;
	}

	@GetMapping("/affiliation")
	public JSONArray affMapViaId(@RequestParam(name = "id") String id) {
		JSONArray array = new JSONArray();
		return array;
	}
}
