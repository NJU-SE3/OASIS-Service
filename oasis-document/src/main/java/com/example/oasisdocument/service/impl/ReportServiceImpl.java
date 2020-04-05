package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.analysis.AuthorCitation;
import com.example.oasisdocument.model.docs.analysis.NormalBuffer;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.repository.analysis.AuthorCitationRepo;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.service.BaseService;
import com.example.oasisdocument.service.CounterService;
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

    @Autowired
    private CounterService counterService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private GeneralJsonVO generalJsonVO;

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
//    @Cacheable(cacheNames = "getPaperRankViaCitation", unless = "#result==null")
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
    public List<Pair<String, List<Paper>>> getAuthorOfMostCitation(int rank) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "citationCount"));
        List<AuthorCitation> buffers =
                mongoTemplate.find(query.with(PageRequest.of(0, rank)), AuthorCitation.class);
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

    @Override
//    @Cacheable(cacheNames = "getPaperTrend", unless = "#result==null")
    public List<Pair<Integer, Double>> getPaperTrend(String type, String id) {
        //指标类型. 可以取值 count , heat , citation , activeness , H_index
        //把所有年份相关的查询
        List<CounterBaseEntity> baseEntities =
                mongoTemplate.find(Query.query(new Criteria("checkId").is(id).and("year").ne(-1)),
                        CounterBaseEntity.class);
        //如果还未初始化每年的数据
        if (baseEntities.isEmpty()) {
            counterService.initCounterPOJO(id);
            baseEntities =
                    mongoTemplate.find(Query.query(new Criteria("checkId").is(id).and("year").ne(-1)),
                            CounterBaseEntity.class);
        }
        //按照年份排序
        baseEntities.sort(Comparator.comparingInt(CounterBaseEntity::getYear));
        switch (type) {
            case "count":
                return baseEntities.stream()
                        .map((CounterBaseEntity en) -> new Pair<>(en.getYear(), (double) en.getPaperCount()))
                        .collect(Collectors.toList());
            case "heat":
                return baseEntities.stream()
                        .map((CounterBaseEntity en) -> new Pair<>(en.getYear(), en.getHeat()))
                        .collect(Collectors.toList());
            case "citation":
                return baseEntities.stream()
                        .map((CounterBaseEntity en) -> new Pair<>(en.getYear(), (double) en.getCitationCount()))
                        .collect(Collectors.toList());
            case "activeness":
                return baseEntities.stream()
                        .map((CounterBaseEntity en) -> new Pair<>(en.getYear(), en.getActiveness()))
                        .collect(Collectors.toList());
            case "H_index":
                return baseEntities.stream()
                        .map((CounterBaseEntity en) -> new Pair<>(en.getYear(), (double) en.getH_index()))
                        .collect(Collectors.toList());
            default:
                throw new BadReqException();
        }
    }

    @Override
    public JSONObject getRankViaType(String type, int pageNum, int pageSize) {
        List<NormalBuffer> buffers = mongoTemplate.find(Query.query(new Criteria("type").is(type)),
                NormalBuffer.class);
        //Not init yet
        JSONArray array = new JSONArray();
        long itemCnt = 0;

        if (buffers.isEmpty()) {
            // On the one hand , return the data back
            switch (type) {
                case "author":
                    //init the true data async
                    baseService.initAuthorRanks();
                    List<Author> authorList = mongoTemplate.find(
                            new Query().with(PageRequest.of(pageNum, pageSize)), Author.class
                    );
                    for (Author author : authorList) {
                        CounterBaseEntity baseEntity = counterService.getSummaryInfo(author.getId());
                        array.add(generalJsonVO.author2VO(author, baseEntity));
                    }
                    itemCnt = mongoTemplate.count(new Query(), Author.class);
                    break;
                case "affiliation":
                    baseService.initAffiliationRanks();

                    List<Affiliation> affiliationList = mongoTemplate.find(
                            new Query().with(PageRequest.of(pageNum, pageSize)), Affiliation.class
                    );
                    for (Affiliation affiliation : affiliationList) {
                        CounterBaseEntity baseEntity = counterService.getSummaryInfo(affiliation.getId());
                        array.add(generalJsonVO.affiliation2VO(affiliation, baseEntity));
                    }
                    itemCnt = mongoTemplate.count(new Query(), Affiliation.class);

                    break;
                case "field":
                    baseService.initFieldRanks();
                    List<Field> fieldList = mongoTemplate.find(
                            new Query().with(PageRequest.of(pageNum, pageSize)), Field.class
                    );
                    for (Field field : fieldList) {
                        CounterBaseEntity baseEntity = counterService.getSummaryInfo(field.getId());
                        array.add(generalJsonVO.field2VO(field, baseEntity));
                    }
                    itemCnt = mongoTemplate.count(new Query(), Field.class);
                    break;
                case "conference":
                    baseService.initConferenceRanks();
                    List<Conference> conferenceList = mongoTemplate.find(
                            new Query().with(PageRequest.of(pageNum, pageSize)), Conference.class
                    );
                    for (Conference conference : conferenceList) {
                        CounterBaseEntity baseEntity = counterService.getSummaryInfo(conference.getId());
                        array.add(generalJsonVO.conference2VO(conference, baseEntity));
                    }
                    itemCnt = mongoTemplate.count(new Query(), Conference.class);
                    break;
                default:
                    throw new BadReqException();
            }
        } else {
            for (NormalBuffer buf : buffers) {
                JSONObject obj = (JSONObject) JSON.parse(buf.getContent());
                array.add(obj);
            }
        }
        JSONArray arr = pageHelper.sortAndPage(array, 0, -1);
        JSONObject ans = new JSONObject();
        ans.put("data",arr);
        ans.put("itemCnt",itemCnt);
        return ans;
    }
}
