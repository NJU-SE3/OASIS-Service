package com.example.oasispaper.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasispaper.model.VO.PaperQueryVO;
import com.example.oasispaper.service.PaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
public class PaperCtl {
    private static final Logger logger = LoggerFactory.getLogger(PaperCtl.class);
    @Autowired
    private PaperService paperService;

    /**
     * 全局查询论文
     *
     * @param query    : 关键字
     * @param pageSize : 每一页大小,默认10
     * @param pageNum  : 页号,从0开始
     * @return : paper VO 列表
     */
    @GetMapping(path = "/paper/list")
    public JSONArray paperQuery(@RequestParam(name = "query") String query,
                                @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        JSONArray res = new JSONArray();
        res.addAll(paperService.paperQuery(query, pageNum, pageSize));
        return res;
    }

    @PostMapping(path = "/paper")
    public void insertPaper(@RequestBody PaperQueryVO paper) {
        paperService.insertPaperVO(paper);
    }


}
