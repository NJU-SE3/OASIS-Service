package com.example.oasisdocument.controller;

import com.example.oasisdocument.service.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 分析数据初始化接口集合
 * 均设置为异步请求类型
 */
@RestController
@RequestMapping("/data/initialization")
public class DataAnalysisInitController {
	@Autowired
	private InitializationService initializationService;

	/**
	 * 机构初始化
	 */
	@PostMapping("/affiliation/base")
	public void initAffiliationBasic() {
		initializationService.initAffiliationBase();
	}

	/**
	 * 会议conference初始化
	 */
	@PostMapping("/conference/base")
	public void initConferenceBasic() {
		initializationService.initConferenceBasic();
	}

	/**
	 * 领域初始化
	 */
	@PostMapping("/field/base")
	public void initFieldBasic() {
		initializationService.initFieldBasic();
	}

	/**
	 * 计数分析初始化
	 */
	@PostMapping("/counter/base")
	public void initCounterPOJO() {
		initializationService.initCounterPOJO();
	}
}
