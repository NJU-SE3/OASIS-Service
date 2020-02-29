package com.example.oasisdocument.service;


import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.utils.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Pair<String, Integer>> getWordCloudOfYear(int year);

    List<Paper> getPaperRankViaCitation(int rank);

    List<Pair<Integer,Integer>> getPaperTrend();

    Map<String, List<Paper>> getAuthorOfMostPaper(int rank);

    List<Paper> getPapersViaAuthor(String authorName);
}
