package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaperController {
    @Autowired
    private PaperService paperService;

    @GetMapping("/")
    public String run() {
        return "hello world";
    }

    @GetMapping("/paper/list")
    public JSONArray queryPaper(@RequestParam(name = "query") String query,
                                  @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        List<Paper> list = paperService.queryPaper(query, pageNum, pageSize);
        return JSONArray.parseArray(JSON.toJSONString(list));
    }

    @PostMapping(path = "/paper")
    public void insertPaper(@RequestBody JSONObject paper) {
        Paper entity = paper.toJavaObject(Paper.class);
        paperService.insert(entity);
    }

}
