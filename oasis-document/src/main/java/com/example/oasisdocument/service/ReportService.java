package com.example.oasisdocument.service;


import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.utils.Pair;

import java.util.List;

public interface ReportService {
    /**
     * 获取以年为单位的词云
     *
     * @param year : 指定的年
     */
    List<Pair<String, Integer>> getWordCloudOfYear(int year);

    /**
     * 获取paper rank , 按照引用数来进行排序
     *
     * @param rank : 前top值
     */
    List<Paper> getPaperRankViaCitation(int rank);

    /**
     * 获取论文数量变化趋势(每一年)
     */
    List<Pair<Integer, Integer>> getPaperTrend();

    /**
     * 获取引用数最多作者排名
     */
    List<Pair<String, List<Paper>>> getAuthorOfMostCitation(int rank);


    List<Paper> getPapersViaAuthor(String authorName);

    void constructPaperCitations();

    /**
     * 获取趋势
     *
     * @param type : 指标类型. 可以取值 count , heat , citation , activeness , H_index
     */
    List<Pair<Integer, Double>> getPaperTrend(String type, String id);
}
