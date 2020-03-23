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
	@PostMapping("/detail")
	public void initAffiliationBasic() {
		initializationService.initAffiliationBase();
		initializationService.initConferenceBasic();
		initializationService.initFieldBasic();
	}

	/**
	 * 计数分析初始化
	 * 由于时间耗费较大,这一步进行每一个实体的summary初始化, 而不精确到年份
	 */
	@PostMapping("/counter/base")
	public void initCounterPOJO() {
		initializationService.initCounterPOJOSummary();
	}
}
