package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.AuthorNodeVO;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//专门用于数据导入
//图数据导入
@RestController
@RequestMapping("/paper/anl")
@EnableAsync
public class PaperGraphController {
	@Autowired
	private GraphService graphService;

	//import author.csv
	@PostMapping("/authors")
	public void importAuthors() {
		try {
			//反序列化
			graphService.importAuthorBasic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
