package com.example.oasisdocument.controller;

import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
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

	//构建图数据库
	@PostMapping("/")
	public void constructGraph() {
		//根据已有的mongodb数据库进行数据的更新
		graphService.constructGraph();
	}
}
