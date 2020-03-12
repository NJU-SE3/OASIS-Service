package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.repository.PaperRepository;
import com.example.oasisdocument.service.ReportService;
import com.example.oasisdocument.utils.PageHelper;
import com.example.oasisdocument.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PageHelper pageHelper;

    @Override
    @Cacheable(cacheNames = "getWordCloudOfYear", key = "#year", unless = "#result==null")
    public List<Pair<String, Integer>> getWordCloudOfYear(int year) {
        //词云的下阈值
        final int lowerBound = 2;

        List<Paper> papers = paperRepository.findAllByYear(year);
        Map<String, Integer> map =
                new ConcurrentHashMap<>();
        //construct
        for (Paper paper : papers) {
            String[] terms = paper.getTerms().split(";");
            for (String s : terms) {
                s = s.trim();
                map.put(s, map.getOrDefault(s, 0) + 1);
            }
        }
        //去除长尾
        map.entrySet().removeIf(entry -> entry.getValue() <= lowerBound);
        List<Pair<String, Integer>> sumUp = new LinkedList<>();
        for (String word : map.keySet()) {
            sumUp.add(new Pair<>(word, map.get(word)));
        }
        sumUp.sort((o1, o2) -> o2.getSecond() - o1.getSecond());

        return sumUp;
    }

    //paper citation排名
    @Override
    @Cacheable(cacheNames = "getPaperRankViaCitation", key = "#rank", unless = "#result==null")
    public List<Paper> getPaperRankViaCitation(int rank) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "citationCount"));
        return mongoTemplate.find(query.with(PageRequest.of(0, rank)), Paper.class);
    }

    //paper trend via year
    @Override
    @Cacheable(cacheNames = "getPaperTrend", unless = "#result==null")
    public List<Pair<Integer, Integer>> getPaperTrend() {
        Map<Integer, Integer> hash = new HashMap<>();
        paperRepository.findAll()
                .forEach((Paper p) -> hash.put(p.getYear(), hash.getOrDefault(p.getYear(), 0) + 1));
        return hash.keySet().stream()
                .map((Integer key) -> new Pair<>(key, hash.get(key)))
                .sorted(Comparator.comparingInt(Pair::getFirst))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "getAuthorOfMostPaper", key = "#rank", unless = "#result==null")
    public List<Pair<String, List<Paper>>> getAuthorOfMostPaper(int rank) {
        //find authors
        Set<String> authorNames = new HashSet<>();
        paperRepository.findAll()
                .forEach((Paper p) -> {
                    String[] names = p.getAuthors().split(";");
                    for (String name : names) {
                        name = name.trim();
                        if (!name.isEmpty())
                            authorNames.add(name);
                    }
                });
        Map<String, List<Paper>> mapAuthorPapers = new HashMap<>();
        List<Pair<String, Integer>> pairs = new LinkedList<>();

        for (String name : authorNames) {
            List<Paper> papers = getPapersViaAuthor(name);
            int sum = 0 ;
            for(Paper paper : papers) sum += paper.getCitationCount();
            mapAuthorPapers.put(name, papers);
            pairs.add(new Pair<>(name, sum));
        }
        pairs.sort((o1, o2) -> o2.getSecond() - o1.getSecond());
        List<Pair<String, List<Paper>>> res =
                new LinkedList<>();
        for (int i = 0; i < Math.min(rank, pairs.size()); ++i) {
            String name = pairs.get(i).getFirst();
            res.add(new Pair<>(name, mapAuthorPapers.get(name)));
        }
        return res;
    }

    @Override
    @Cacheable(cacheNames = "getPapersViaAuthor", key = "#authorName", unless = "#result==null")
    public List<Paper> getPapersViaAuthor(String authorName) {
        final int limit = 5;
        Query query = new Query(Criteria.where("authors").regex(authorName));
        List<Paper> ans = mongoTemplate.find(query, Paper.class);
        ans.sort((o1, o2) -> o2.getCitationCount() - o1.getCitationCount());
        ans = ans.subList(0, Math.min(limit, ans.size()));
        return new LinkedList<>(ans);
    }


}
