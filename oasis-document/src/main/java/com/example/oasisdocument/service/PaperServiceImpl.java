package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
//    @Cacheable(cacheNames = "paperQuery", unless = "#result==null")
    public List<Paper> queryPaper(List<String> keys, String returnFacets) throws BadReqException {
        List<Paper> papers = paperRepository.findAll();
        papers = papers.stream()
                .filter((Paper p) -> {
                    String line = paper2Str(p, returnFacets);
                    for (String key : keys) {
                        if (!line.contains(key)) return false;
                    }
                    return true;
                }).collect(Collectors.toList());
        return papers;
    }

    @Override
    public void insert(Paper entity) {
        paperRepository.save(entity);
    }

    @Override
//    @Cacheable(cacheNames = "queryPaperRefine", unless = "#result==null")
    public List<Paper> queryPaperRefine(List<Paper> papers, List<String> refinements) {
        //conference , term , author , affiliation , year
        Map<String, List<String>> hash = refineAnalysis(refinements);
        papers = papers.stream()
                .filter((Paper paper) -> {
                    if (!hash.containsKey("year")) return true;
                    String val = hash.get("year").get(0);
                    String[] set = val.split("_");
                    if (set.length != 2) throw new BadReqException();
                    int start = Integer.parseInt(set[0]),
                            end = Integer.parseInt(set[1]);
                    return paper.getYear() <= end && paper.getYear() >= start;
                })
                .filter((Paper paper) -> {
                    if (!hash.containsKey("author")) return true;
                    String line = paper.getAuthors();
                    for (String v : hash.get("author")) {
                        if (line.contains(v)) return true;
                    }
                    return false;
                })
                .filter((Paper paper) -> {
                    if (!hash.containsKey("term")) return true;
                    String line = paper.getTerms();
                    for (String v : hash.get("term")) {
                        if (line.contains(v)) return true;
                    }
                    return false;
                })
                .filter((Paper paper) -> {
                    if (!hash.containsKey("conference")) return true;
                    String line = paper.getTerms();
                    for (String v : hash.get("conference")) {
                        if (line.contains(v)) return true;
                    }
                    return false;
                })
                .filter((Paper paper) -> {
                    if (!hash.containsKey("affiliation")) return true;
                    String line = paper.getTerms();
                    for (String v : hash.get("affiliation")) {
                        if (line.contains(v)) return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        return papers;
    }

    private Map<String, List<String>> refineAnalysis(final List<String> refinements) {
        Map<String, List<String>> ans = new HashMap<>();
        for (String refine : refinements) {
            String[] sp = refine.split(":");
            if (sp.length != 2) throw new BadReqException();
            sp[0] = sp[0].toLowerCase();    //小写
            List<String> value = ans.getOrDefault(sp[0], new LinkedList<>());
            value.add(sp[1]);
            ans.put(sp[0], value);
        }
        return ans;
    }

    private String paper2Str(Paper paper, String returnFacets) throws BadReqException {
        StringBuilder sb = new StringBuilder();
        switch (returnFacets) {
            case "all":
                sb.append(paper.getAffiliations()).append(paper.getTitle()).append(paper.getConference())
                        .append(paper.getAuthors()).append(paper.getKeywords()).append(paper.getTerms());
                break;
            case "title":
                sb.append(paper.getTitle());
                break;
            case "conferences":
                sb.append(paper.getConference());
                break;

            case "terms":
                sb.append(paper.getTerms());
                break;

            case "keywords":
                sb.append(paper.getKeywords());
                break;

            case "authors":
                sb.append(paper.getAuthors());
                break;

            case "affiliations":
                sb.append(paper.getAffiliations());
                break;
            default:
                throw new BadReqException();
        }
        return sb.toString();
    }

    private Criteria getQueryCriteria(String key) {
        List<String> fields = Arrays.asList("title", "conference", "terms",
                "keywords", "authors", "affiliation");
        List<Criteria> criterias = fields.stream()
                .map((String name) -> Criteria.where(name).regex(key))
                .collect(Collectors.toList());
        Criteria ans = new Criteria();
        ans.orOperator(criterias.get(0), criterias.get(1), criterias.get(2),
                criterias.get(3), criterias.get(4), criterias.get(5));
        return ans;
    }
}
