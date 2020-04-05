package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.PaperService;
import com.example.oasisdocument.utils.CookieUtil;
import com.example.oasisdocument.utils.PageHelper;
import com.example.oasisdocument.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaperServiceImpl implements PaperService {
    private static final String authorSplitter = ";";
    private static final String affiliationSplitter = ";";
    private static final String termSplitter = ",";

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CounterService counterService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GeneralJsonVO generalJsonVO;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private PageHelper pageHelper;

    /**
     * 内部类, 专门用于实现
     */
    private static class PaperRanker implements Comparator<Paper> {
        private String key;
        private static final int[] rankRules = {1_000_000_000, 1_000_000, 1_000, 500, 1, 1};

        public PaperRanker(String key) {
            this.key = key;
        }

        @Override
        public int compare(Paper p1, Paper p2) {
            long r1 = getRank(p1), r2 = getRank(p2);
            return Math.toIntExact(r2 - r1);
        }

        private long getRank(Paper paper) {
            long rank = 0;
            if (paper.getTitle().contains(key)) {
                rank += rankRules[0];
            }
            if (paper.getAuthors().contains(key)) rank += rankRules[1];
            if (paper.getAffiliations().contains(key)) rank += rankRules[2];
            if (paper.getConference().contains(key)) rank += rankRules[3];
            if (paper.getKeywords().contains(key)) rank += rankRules[4];
            if (paper.getTerms().contains(key)) rank += rankRules[5];
            return rank;
        }
    }

    @Override
    @Cacheable(cacheNames = "queryPaper", unless = "#result==null")
    public JSONObject queryPaper(final String key, final String returnFacets,
                                 int pageSize, int pageNum,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws BadReqException {
        //set id
        String qid = UUID.randomUUID().toString().replaceAll("-", "");
        Criteria criteria = fetchCriteriaViaKey(key, returnFacets);
        List<Paper> papers = mongoTemplate.find(new Query(criteria), Paper.class);
        if (returnFacets.equals("all"))
            papers.sort(new PaperRanker(key));
        List<String> wholeList = papers.stream().map(JSON::toJSONString).collect(Collectors.toList());

        if (wholeList.isEmpty()) {
            JSONObject ans = new JSONObject();
            ans.put("papers", wholeList);
            ans.put("itemCnt", wholeList.size());
            return ans;
        }
        //分页
        List<String> pagedList = pageHelper.of(wholeList, pageSize, pageNum);
        if (null == pagedList) throw new BadReqException();
        JSONArray arr = new JSONArray();
        //convert from paged list
        pagedList.forEach((String str) -> {
            Paper paper = JSON.parseObject(str, Paper.class);
            arr.add(generalJsonVO.paper2BriefVO(paper));
        });
        //Store into the ids
        request.getSession().setAttribute(qid, wholeList);
        cookieUtil.set(response, "qid", qid);
        JSONObject ans = new JSONObject();
        ans.put("papers", arr);
        ans.put("itemCnt", wholeList.size());
        return ans;
    }

    //paper 导入
    @Override
    @Async
    public void insertPaperVOEntity(PaperInsertVO entity) {
        if (null == mongoTemplate.findById(entity.getId(), Paper.class)) {
            List<String> array = entity.getAuthorIds().stream().map(String::valueOf).collect(Collectors.toList());
            List<Author> authors = new LinkedList<>();
            //author 设置
            if (!array.isEmpty()) {
                List<String> affNameList = new LinkedList<>(),
                        authorNameList = new LinkedList<>();
                for (String authorId : array) {
                    Author author = mongoTemplate.findById(authorId, Author.class);
                    if (null != author) {
                        String aff = author.getAffiliationName();
                        String authorName = author.getAuthorName();
                        affNameList.add(aff);
                        authorNameList.add(authorName);
                    }
                }
                entity.setAffiliations(String.join(affiliationSplitter, affNameList));
                entity.setAuthors(String.join(authorSplitter, authorNameList));
            }
            Paper paper = entity.VO2PO();
            //conference 设置
            Conference conEn = mongoTemplate.findOne(Query.query(new Criteria("conferenceName")
                            .is(paper.getConference()).and("year").is(entity.getYear())),
                    Conference.class);
            if (null == conEn) {
                if (!paper.getConference().isEmpty()) {
                    conEn = new Conference();
                    conEn.setConferenceName(paper.getConference());
                    conEn.setYear(entity.getYear());
                    conEn = mongoTemplate.save(conEn);
                }
            }
            paper.setConferenceEntity(conEn);
            paperRepository.save(paper);
        }
    }

    @Override
    @Cacheable(cacheNames = "queryPaperRefine", unless = "#result==null")
    public JSONObject queryPaperRefine(String qid, List<String> refinements,
                                       int pageNum, int pageSize, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //查询全集
        List<String> cachedList = (List<String>) session.getAttribute(qid);
        //添加新限制
        JSONArray array = queryPaperRefine(cachedList, refinements);
        if (cachedList.isEmpty()) {
            JSONObject obj = new JSONObject();
            obj.put("papers", cachedList);
            obj.put("itemCnt", cachedList.size());
            return obj;
        }
        //需要用到分页信息
        List<Object> pagedList = pageHelper.of(array, pageSize, pageNum);
        if (null == pagedList) throw new BadReqException();
        JSONArray paperArr = pagedList.stream()
                .map((Object line) -> {
                    Paper paper = JSON.parseObject(String.valueOf(line), Paper.class);
                    return generalJsonVO.paper2BriefVO(paper);
                }).collect(Collectors.toCollection(JSONArray::new));
        // return
        JSONObject ans = new JSONObject();
        ans.put("papers", paperArr);
        ans.put("itemCnt", cachedList.size());
        return ans;

    }

    @Override
    @Cacheable(cacheNames = "papersSummary", unless = "#result==null")
    public JSONObject papersSummary(String qid, HttpServletRequest request) {
        //conference , term , author , affiliation
        List<String> list = (List<String>) request.getSession().getAttribute(qid);

        JSONObject ans = new JSONObject();
        final int limit = 5;
        if (list == null || list.isEmpty()) return ans;
        List<PaperBriefVO> papers = new LinkedList<>();
        list.forEach((String obj) -> {
            PaperBriefVO vo = JSON.parseObject(obj, PaperBriefVO.class);
            papers.add(vo);
        });
        //author
        final Map<String, Integer> authorHash = new HashMap<>();
        papers.forEach((PaperBriefVO p) -> {
            Set<String> authorNames = Arrays.stream(p.getAuthors().split(authorSplitter))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            for (String name : authorNames) {
                authorHash.put(name, authorHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("author", transformHash(authorHash, 5));
        //conference
        final Map<String, Integer> conferHash = new HashMap<>();
        papers.forEach((PaperBriefVO p) -> {
            String name = p.getConference();
            conferHash.put(name, conferHash.getOrDefault(name, 0) + 1);
        });
        ans.put("conference", transformHash(conferHash, 5));
        //term
        final Map<String, Integer> termHash = new HashMap<>();
        papers.forEach((PaperBriefVO paper) -> {
            Set<String> termNames = Arrays.stream(paper.getTerms().split(termSplitter))
                    .map(String::trim).collect(Collectors.toSet());
            for (String name : termNames) {
                termHash.put(name, termHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("term", transformHash(termHash, 10));
        //affiliation
        final Map<String, Integer> affiliationHash = new HashMap<>();
        papers.forEach((PaperBriefVO p) -> {
            Set<String> affiliationNames = Arrays.stream(p.getAffiliations().split(affiliationSplitter))
                    .map(String::trim)
                    .filter((String s) -> !s.equals(""))
                    .collect(Collectors.toSet());
            for (String name : affiliationNames) {
                affiliationHash.put(name, affiliationHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("affiliation", transformHash(affiliationHash, 5));
        //year
        final Map<String, Integer> yearHash = new HashMap<>();
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (PaperBriefVO vo : papers) {
            min = Math.min(vo.getYear(), min);
            max = Math.max(vo.getYear(), max);
        }
        yearHash.put("beginYear", min);
        yearHash.put("endYear", max);
        ans.put("yearRange", yearHash);
        return ans;
    }

    @Override
    @Cacheable(cacheNames = "fetchPaperList", unless = "#result==null")
    public List<PaperBriefVO> fetchPaperList(String id) {
        //获取分析数据
        CounterBaseEntity en = counterService.getSummaryInfo(id);
        List<Paper> papers = new LinkedList<>();
        en.getPaperList().forEach((String pid) -> papers.addAll(paperRepository.findAllById(pid)));
        List<PaperBriefVO> ans = papers.stream()
                .map(PaperBriefVO::PO2VO)
                .collect(Collectors.toList());
        //Sort by citation count
        ans.sort((o1, o2) -> o2.getCitationCount() - o1.getCitationCount());
        return ans;
    }

    private JSONArray queryPaperRefine(List<String> papers, List<String> refinements) {
        //conference , term , author , affiliation , year
        Map<String, List<String>> hash = refineAnalysis(refinements);
        JSONArray ans = new JSONArray();
        if (null == papers || papers.isEmpty()) return new JSONArray();
        if (hash.containsKey("year")) {
            ans = papers.stream()
                    .filter((String line) -> {
                        String val = hash.get("year").get(0);
                        String[] set = val.split("_");
                        if (set.length != 2) throw new BadReqException();
                        int start = Integer.parseInt(set[0]),
                                end = Integer.parseInt(set[1]);
                        int year = JSON.parseObject(line).getInteger("year");
                        return year <= end && year >= start;
                    })
                    .map(JSON::parse)
                    .collect(Collectors.toCollection(JSONArray::new));
            if (papers.isEmpty()) return ans;
        }
        if (hash.containsKey("author")) {
            ans = papers.stream().filter((String obj) -> {
                String line = JSON.parseObject(obj).getString("authors");
                for (String v : hash.get("author")) {
                    if (line.contains(v)) return true;
                }
                return false;
            }).map(JSON::parse)
                    .collect(Collectors.toCollection(JSONArray::new));
            if (papers.isEmpty()) return ans;
        }

        if (hash.containsKey("term")) {
            ans = papers.stream().filter((String obj) -> {
                String termsLine = JSON.parseObject(obj).getString("terms");
                Set<String> targetSet = Arrays.stream(termsLine.split(termSplitter))
                        .map(String::trim).collect(Collectors.toSet());
                for (String v : hash.get("term")) {
                    if (targetSet.contains(v)) return true;
                }
                return false;
            }).map(JSON::parse)
                    .collect(Collectors.toCollection(JSONArray::new));
            if (papers.isEmpty()) return ans;
        }
        if (hash.containsKey("conference")) {
            ans = papers.stream().filter((String obj) -> {
                String line = JSON.parseObject(obj).getString("conference");
                for (String v : hash.get("conference")) {
                    if (line.contains(v)) return true;
                }
                return false;
            }).map(JSON::parse)
                    .collect(Collectors.toCollection(JSONArray::new));
            if (papers.isEmpty()) return ans;
        }
        if (hash.containsKey("affiliation")) {
            ans = papers.stream().filter((String obj) -> {
                String line = JSON.parseObject(obj).getString("affiliations");
                Set<String> targetSet = Arrays.stream(line.split(affiliationSplitter))
                        .map(String::trim)
                        .collect(Collectors.toSet());
                for (String v : hash.get("affiliation")) {
                    if (targetSet.contains(v)) return true;
                }
                return false;
            }).map(JSON::parse)
                    .collect(Collectors.toCollection(JSONArray::new));
        }
        return ans;
    }

    private Map<String, List<String>> refineAnalysis(final List<String> refinements) {
        Map<String, List<String>> ans = new HashMap<>();
        for (String refine : refinements) {
            //year:name
            String[] sp = refine.split(":");
            if (sp.length != 2) throw new BadReqException();
            sp[0] = sp[0].toLowerCase();    //小写
            List<String> value = ans.getOrDefault(sp[0], new LinkedList<>());
            value.add(sp[1]);
            ans.put(sp[0], value);
        }
        return ans;
    }

    private Criteria fetchCriteriaViaKey(String key, String returnFacets) throws BadReqException {
        Criteria criteria;
        switch (returnFacets) {
            case "all":
                criteria = getQueryCriteria(key);
                break;
            case "title":
                criteria = Criteria.where("title").regex(key,"i");
                break;
            case "conferences":
                criteria = Criteria.where("conference").regex(key,"i");
                break;
            case "terms":
                criteria = Criteria.where("terms").regex(key,"i");
                break;
            case "keywords":
                criteria = Criteria.where("keywords").regex(key,"i");
                break;
            case "authors":
                criteria = Criteria.where("authors").regex(key,"i");
                break;
            case "affiliations":
                criteria = Criteria.where("affiliations").regex(key,"i");
                break;
            default:
                throw new BadReqException();
        }
        return criteria;
    }

    private Criteria getQueryCriteria(String key) {
        List<String> fields = Arrays.asList("title", "conference", "terms",
                "keywords", "authors", "affiliations");
        List<Criteria> criterias = fields.stream()
                .map((String name) -> Criteria.where(name).regex(key,"i"))
                .collect(Collectors.toList());
        Criteria ans = new Criteria();
        ans.orOperator(criterias.get(0), criterias.get(1), criterias.get(2),
                criterias.get(3), criterias.get(4), criterias.get(5));
        return ans;
    }

    private <K> List<Pair<K, Integer>> transformHash(Map<K, Integer> hash, final int limit) {
        List<Pair<K, Integer>> ans = new LinkedList<>();
        for (K k : hash.keySet()) {
            ans.add(new Pair<>(k, hash.get(k)));
        }
        ans.sort((o1, o2) -> o2.getSecond() - o1.getSecond());
        ans = ans.subList(0, Math.min(ans.size(), limit));
        return new LinkedList<>(ans);
    }
}
