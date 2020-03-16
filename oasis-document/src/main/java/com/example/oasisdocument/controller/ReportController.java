package com.example.oasisdocument.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.service.ReportService;
import com.example.oasisdocument.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 具体报表信息统计
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    //论文总数折线图 , 按照年份排
    @GetMapping("/paper/trend/year")
    public JSONArray getPaperTrend() {
        return reportService.getPaperTrend().stream()
                .map((Pair<Integer, Integer> pair) -> {
                    JSONObject object = new JSONObject();
                    object.put("year", String.valueOf(pair.getFirst()));
                    object.put("count", String.valueOf(pair.getSecond()));
                    return object;
                }).collect(Collectors.toCollection(JSONArray::new));
    }

    //被引用论文数最多作者TOP10的堆叠柱状图
    @GetMapping("/author/rank/paper_cnt")
    public JSONArray getAuthorOfMostPaper(@RequestParam(name = "rank", defaultValue = "10") int rank) {
        List<Pair<String, List<Paper>>> list = reportService.getAuthorOfMostPaper(rank);
        return list.stream()
                .map((Pair<String, List<Paper>> pair) -> {
                    List<Paper> papers = pair.getSecond();
                    JSONObject object = new JSONObject();
                    object.put("author", pair.getFirst());
                    object.put("papers", papers.stream()
                            .map(this::simplifyPaper)
                            .collect(Collectors.toCollection(JSONArray::new)));
                    return object;
                }).collect(Collectors.toCollection(JSONArray::new));
    }

    @PostMapping("/author/rank/paper_cnt")
    public void constructPaperCitations() {
        reportService.constructPaperCitations();
    }

    //被引用数最多的论文TOP K
    @GetMapping("/paper/rank/citation")
    public List<JSONObject> getPaperRankViaCitation(@RequestParam(name = "rank", defaultValue = "10") int rank) {
        return reportService.getPaperRankViaCitation(rank).stream()
                .map(this::simplifyPaper)
                .collect(Collectors.toList());
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
        ans.put("citationCount", String.valueOf(paper.getCitationCount()));
        ans.put("pdfLink", paper.getPdfLink());
        ans.put("year", String.valueOf(paper.getYear()));
        return ans;
    }
}
