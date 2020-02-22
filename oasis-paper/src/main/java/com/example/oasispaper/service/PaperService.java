package com.example.oasispaper.service;

import com.example.oasispaper.model.VO.PaperQueryVO;

import java.util.List;

public interface PaperService {
    List<PaperQueryVO> paperQuery(String key, int pageNum, int pageSize);
}
