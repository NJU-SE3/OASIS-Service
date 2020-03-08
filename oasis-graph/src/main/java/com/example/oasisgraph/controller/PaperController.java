package com.example.oasisgraph.controller;

import com.example.oasisgraph.VO.PaperVO;
import com.example.oasisgraph.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paper")
public class PaperController {
	@Autowired
	private PaperService paperService;

	//paper 导入,图构建
	@PostMapping("/basic")
	public void insertPaper(@RequestBody PaperVO paperVO) {
		paperService.insertNewPaper(paperVO);
	}
}
