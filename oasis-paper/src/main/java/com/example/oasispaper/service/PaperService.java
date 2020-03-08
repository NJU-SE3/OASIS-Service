package com.example.oasispaper.service;

import com.example.oasispaper.model.VO.PaperVO;

import java.util.List;

public interface PaperService {
	/**
	 * 全关键字搜索
	 *
	 * @return
	 */
	List<PaperVO> paperQuery(String key, int pageNum, int pageSize);

	void insertPaperVO(PaperVO paper);
}
