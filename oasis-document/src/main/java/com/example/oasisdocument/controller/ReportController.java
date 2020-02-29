package com.example.oasisdocument.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.service.ReportService;
import com.example.oasisdocument.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 具体报表信息统计
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    //论文总数折线图 , 按照年份排
    @GetMapping("/paper/trend/year")
    public JSONArray getPaperTrend() {
        return reportService.getPaperTrend().stream()
                .map((Pair<Integer, Integer> pair) -> {
                    JSONObject object = new JSONObject();
                    object.put("year", pair.getFirst());
                    object.put("count", pair.getSecond());
                    return object;
                }).collect(Collectors.toCollection(JSONArray::new));
    }

    //被引用论文数最多作者TOP10的堆叠柱状图
    //TODO: 修改为持久化, 并且删减paper内容
    @GetMapping("/author/rank/paper_cnt")
    public JSONObject getAuthorOfMostPaper(@RequestParam(name = "rank", defaultValue = "10") int rank) {
        Map<String, List<Paper>> hash = reportService.getAuthorOfMostPaper(rank);
        JSONObject ans = new JSONObject();
        for (String key : hash.keySet()) {
            List<Paper> papers = hash.get(key);
            JSONArray array = papers.stream()
                    .map(this::simplifyPaper)
                    .collect(Collectors.toCollection(JSONArray::new));
            ans.put(key, array);
        }
        return ans;
    }

    //被引用数最多的论文TOP K
    @GetMapping("/paper/rank/citation")
    public List<JSONObject> getPaperRankViaCitation(@RequestParam(name = "rank", defaultValue = "10") int rank) {
        return reportService.getPaperRankViaCitation(rank).stream()
                .map(this::simplifyPaper).collect(Collectors.toList());
    }


    //年度热门方向（词及热度）的词云
    @GetMapping("/wdcld/year")
    public JSONArray getWordCloudOfYear(@RequestParam(name = "year") int year) {
        assert year >= 0;
        return reportService.getWordCloudOfYear(year).stream()
                .map((Pair<String, Integer> pair) -> {
                    JSONObject object = new JSONObject();
                    object.put("term", pair.getFirst());
                    object.put("count", pair.getSecond());
                    return object;
                }).collect(Collectors.toCollection(JSONArray::new));
    }

    private JSONObject simplifyPaper(Paper paper) {
        JSONObject ans = new JSONObject();
        //标题、作者、引用数、PDF link , year
        ans.put("title", paper.getTitle());
        ans.put("authors", paper.getAuthors());
        ans.put("citationCount", paper.getCitationCount());
        ans.put("pdfLink", paper.getPdfLink());
        ans.put("year", paper.getYear());
        return ans;
    }
}
