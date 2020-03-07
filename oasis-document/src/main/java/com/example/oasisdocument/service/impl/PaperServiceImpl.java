package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.repository.PaperRepository;
import com.example.oasisdocument.service.PaperService;
import com.example.oasisdocument.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 内部类, 专门用于实现
     */
    private static class PaperRanker implements Comparator<Paper> {
        private String key;
        // title , authors , affiliations ,conferences ,  keywords , terms ,
        private static final int[] rankRules = {Integer.MAX_VALUE, 40, 30, 5, 1, 1};

        public PaperRanker(String key) {
            this.key = key;
        }

        @Override
        public int compare(Paper p1, Paper p2) {
            int r1 = getRank(p1), r2 = getRank(p2);
            return r2 - r1;
        }

        private int getRank(Paper paper) {
            int rank = 0;
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
    @Cacheable(cacheNames = "paper", unless = "#result==null")
    public List<Paper> queryPaper(final String key, final String returnFacets)
            throws BadReqException {
        Criteria criteria = fetchCriteriaViaKey(key, returnFacets);
        List<Paper> papers = mongoTemplate.find(new Query(criteria), Paper.class);
        if (returnFacets.equals("all"))
            papers.sort(new PaperRanker(key));
        return papers;
    }

    @Override
    public void insert(Paper entity) {
        paperRepository.save(entity);
    }

    @Override
    public List<Paper> queryPaperRefine(List<Paper> papers, List<String> refinements) {
        //conference , term , author , affiliation , year
        Map<String, List<String>> hash = refineAnalysis(refinements);
        if (papers == null || papers.isEmpty()) return new LinkedList<>();
        if (hash.containsKey("year")) {
            papers = papers.stream()
                    .filter((Paper paper) -> {
                        String val = hash.get("year").get(0);
                        String[] set = val.split("_");
                        if (set.length != 2) throw new BadReqException();
                        int start = Integer.parseInt(set[0]),
                                end = Integer.parseInt(set[1]);
                        return paper.getYear() <= end && paper.getYear() >= start;
                    }).collect(Collectors.toList());
            if (papers.isEmpty()) return papers;
        }
        if (hash.containsKey("author")) {
            papers = papers.stream().filter((Paper paper) -> {
                String line = paper.getAuthors();
                for (String v : hash.get("author")) {
                    if (line.contains(v)) return true;
                }
                return false;
            }).collect(Collectors.toList());
            if (papers.isEmpty()) return papers;
        }

        if (hash.containsKey("term")) {
            papers = papers.stream().filter((Paper paper) -> {
                String line = paper.getTerms();
                Set<String> targetSet = Arrays.stream(line.split(";"))
                        .map(String::trim).collect(Collectors.toSet());
                for (String v : hash.get("term")) {
                    if (targetSet.contains(v)) return true;
                }
                return false;
            }).collect(Collectors.toList());
            if (papers.isEmpty()) return papers;
        }
        if (hash.containsKey("conference")) {
            papers = papers.stream().filter((Paper paper) -> {
                String line = paper.getConference();
                for (String v : hash.get("conference")) {
                    if (line.contains(v)) return true;
                }
                return false;
            }).collect(Collectors.toList());
            if (papers.isEmpty()) return papers;
        }
        if (hash.containsKey("affiliation")) {
            papers = papers.stream().filter((Paper paper) -> {
                String line = paper.getAffiliations();
                Set<String> targetSet = Arrays.stream(line.split(";"))
                        .map(String::trim)
                        .collect(Collectors.toSet());
                for (String v : hash.get("affiliation")) {
                    if (targetSet.contains(v)) return true;
                }
                return false;
            }).collect(Collectors.toList());
        }
        return papers;
    }

    @Override
    public JSONObject papersSummary(List<Paper> papers) {
        //conference , term , author , affiliation
        JSONObject ans = new JSONObject();
        final int limit = 5;
        if (papers == null || papers.isEmpty()) return ans;
        //author
        final Map<String, Integer> authorHash = new HashMap<>();
        papers.forEach((Paper p) -> {
            Set<String> authorNames = Arrays.stream(p.getAuthors().split(";"))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            for (String name : authorNames) {
                authorHash.put(name, authorHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("author", transformHash(authorHash, 5));
        //conference
        final Map<String, Integer> conferHash = new HashMap<>();
        papers.forEach((Paper p) -> {
            String name = p.getConference();
            conferHash.put(name, conferHash.getOrDefault(name, 0) + 1);
        });
        ans.put("conference", transformHash(conferHash, 5));
        //term
        final Map<String, Integer> termHash = new HashMap<>();
        papers.forEach((Paper p) -> {
            Set<String> termNames = Arrays.stream(p.getTerms().split(";"))
                    .map(String::trim).collect(Collectors.toSet());
            for (String name : termNames) {
                termHash.put(name, termHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("term", transformHash(termHash, 10));
        //affiliation
        final Map<String, Integer> affiliationHash = new HashMap<>();
        papers.forEach((Paper p) -> {
            Set<String> affiliationNames = Arrays.stream(p.getAffiliations().split(";"))
                    .map(String::trim)
                    .filter((String s) -> !s.equals(""))
                    .collect(Collectors.toSet());
            for (String name : affiliationNames) {
                affiliationHash.put(name, affiliationHash.getOrDefault(name, 0) + 1);
            }
        });
        ans.put("affiliation", transformHash(affiliationHash, 5));

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
