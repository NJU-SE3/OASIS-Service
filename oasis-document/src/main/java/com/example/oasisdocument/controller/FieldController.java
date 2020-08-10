package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/field")
public class FieldController {
	@Autowired
	private FieldService fieldService;

	@GetMapping("/distribution")
	public JSONArray fetchFieldDistribution(@RequestParam(name = "id") String id) {
		return fieldService.fetchFieldDistribution(id);
	}

	@GetMapping("/search")
	public JSONArray fetchFieldByName(@RequestParam(name = "query")String fieldName,
									  @RequestParam(name = "pageNum",defaultValue = "0")int pageNum,
									  @RequestParam(name = "pageSize",defaultValue = "10") int pageSize){
		return fieldService.fetchFieldByName(fieldName,pageNum,pageSize);
	}
}
