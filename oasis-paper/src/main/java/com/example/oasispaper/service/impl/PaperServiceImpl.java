package com.example.oasispaper.service.impl;

import com.example.oasispaper.mapper.PaperMapper;
import com.example.oasispaper.model.VO.PaperQueryVO;
import com.example.oasispaper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperMapper paperMapper;

    @Override
    public List<PaperQueryVO> paperQuery(String key, int pageNum, int pageSize) {
        return null;
    }
}
