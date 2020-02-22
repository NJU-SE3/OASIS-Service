package com.example.oasispaper.service;

import com.example.oasispaper.model.VO.PaperQueryVO;

import java.util.List;

public interface PaperService {
    /**
     * 全关键字搜索
     */
    List<PaperQueryVO> paperQuery(String key, int pageNum, int pageSize);

    void insertPaperVO(PaperQueryVO paper);
}
