package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.analysis.AuthorCitation;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.repository.analysis.AuthorCitationRepo;
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
    private static final String authorSplitter = ",";
    private static final String affiliationSplitter = ",";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PageHelper pageHelper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorCitationRepo authorCitationRepo;

    @Override
    @Cacheable(cacheNames = "getWordCloudOfYear", unless = "#result==null")
    public List<Pair<String, Integer>> getWordCloudOfYear(int year) {
        //词云的下阈值
        final int lowerBound = 2;

        List<Paper> papers = paperRepository.findAllByYear(year);
        Map<String, Integer> map =
                new ConcurrentHashMap<>();
        //construct
        for (Paper paper : papers) {
            Set<String> terms = Paper.getAllTerms(paper);
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
    @Cacheable(cacheNames = "getAuthorOfMostPaper", unless = "#result==null")
    public List<Pair<String, List<Paper>>> getAuthorOfMostPaper(int rank) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "citationCount"));
        List<AuthorCitation> buffers = mongoTemplate.find(query.with(PageRequest.of(0, rank)), AuthorCitation.class);
        List<Pair<String, List<Paper>>> ans = new LinkedList<>();
        for (AuthorCitation buf : buffers) {
            ans.add(new Pair<>(buf.getAuthorName(), buf.getPapers()));
        }
        return ans;
    }

    @Override
    public List<Paper> getPapersViaAuthor(String authorName) {
        final int limit = 5;
        Query query = new Query(Criteria.where("authors").regex(authorName));
        List<Paper> ans = mongoTemplate.find(query, Paper.class);
        ans.sort((o1, o2) -> o2.getCitationCount() - o1.getCitationCount());
        ans = ans.subList(0, Math.min(limit, ans.size()));
        return new LinkedList<>(ans);
    }

    @Override
    public void constructPaperCitations() {
        //find authors
        List<Author> authorList = authorRepository.findAll();
        for (Author author : authorList) {
            String name = author.getAuthorName();
            List<Paper> papers = getPapersViaAuthor(name);
            int sum = 0;
            for (Paper paper : papers) sum += paper.getCitationCount();
            AuthorCitation buf = new AuthorCitation();
            buf.setAuthorName(name);
            buf.setPapers(papers);
            buf.setCitationCount(sum);
            authorCitationRepo.save(buf);
        }
    }
}
