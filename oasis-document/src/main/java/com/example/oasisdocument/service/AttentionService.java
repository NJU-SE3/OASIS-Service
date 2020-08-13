package com.example.oasisdocument.service;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.options.AttentionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttentionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CounterService counterService;

    /**
     * 作者在某一个领域内的关注度趋势 , 按照年份进行查找
     */
    public List<AttentionDTO> queryAttentionTrend(final String authorId, final String fieldName) {
        Author author = mongoTemplate.findById(authorId, Author.class);
        if (null == author) {
            throw new EntityNotFoundException();
        }
        Field field = queryFieldByName(fieldName);
        if (null == field) {
            throw new EntityNotFoundException();
        }
        List<AttentionDTO> ans = new LinkedList<>();
        List<String> paperIds = counterService.getSummaryInfo(authorId)
                .getPaperList();
        int minYear = Integer.MAX_VALUE, maxYear = 0;
        List<Paper> paperList = new LinkedList<>();
        for (String pid : paperIds) {
            Paper paper = mongoTemplate.findById(pid, Paper.class);
            if (paper == null) continue;
            minYear = Math.min(minYear, paper.getYear());
            maxYear = Math.max(maxYear, paper.getYear());
            paperList.add(paper);
        }
        //年份排序
        paperList.sort(Comparator.comparingInt(Paper::getYear));
        Map<Integer, Integer> hash = new HashMap<>();
        for (Paper paper : paperList) {
            if (paper.getTerms().contains(fieldName)) {
                hash.put(paper.getYear(), hash.getOrDefault(paper.getYear(), 0) + 1);
            }
        }
        for (Integer year : hash.keySet()) {
            AttentionDTO attentionDTO = new AttentionDTO();
            attentionDTO.setYear(year);
            attentionDTO.setScore(hash.getOrDefault(year, 0));
            attentionDTO.setFieldName(fieldName);
            attentionDTO.setAuthor(author);
            attentionDTO.setFieldId(field.getId());
            ans.add(attentionDTO);
        }
        ans.sort(Comparator.comparingInt(AttentionDTO::getYear));
        return ans;
    }

    /**
     * 批量获取最关注领域
     */
    public List<AttentionDTO> batchQueryMaxAttention(final String authorId) {
        Author author = mongoTemplate.findById(authorId, Author.class);
        if (null == author) {
            throw new EntityNotFoundException();
        }
        List<String> papers = counterService.getSummaryInfo(authorId)
                .getPaperList();
        int minYear = Integer.MAX_VALUE, maxYear = 0;
        List<Paper> paperList = new LinkedList<>();
        for (String pid : papers) {
            Paper paper = mongoTemplate.findById(pid, Paper.class);
            if (paper == null) continue;
            minYear = Math.min(minYear, paper.getYear());
            maxYear = Math.max(maxYear, paper.getYear());
            paperList.add(paper);
        }
        List<AttentionDTO> res = new LinkedList<>();
        // 年份排序
        paperList.sort(Comparator.comparingInt(Paper::getYear));
        // 逐年查询
        for (int y = minYear; y <= maxYear; ++y) {
            Map<String, Integer> hash = queryAttentionHash(paperList, y);
            ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(hash.entrySet());
            list.sort((o1, o2) -> o2.getValue() - o1.getValue());
            if (list.isEmpty()) {
                continue;
            }
            int score = list.get(0).getValue();
            String term = list.get(0).getKey();
            Field field = queryFieldByName(term);
            if (field == null) continue;
            AttentionDTO attentionDTO = new AttentionDTO();
            attentionDTO.setAuthor(author);
            attentionDTO.setScore(score);
            attentionDTO.setFieldName(term);
            attentionDTO.setFieldId(field.getId());
            attentionDTO.setYear(y);

            res.add(attentionDTO);
        }
        return res;
    }


    private Map<String, Integer> queryAttentionHash(List<Paper> totalList, int year) {
        Map<String, Integer> ans = new HashMap<>();
        // 二分查询得到
        int l = 0, r = totalList.size() - 1;
        while (l < r) {
            int mid = l + r - 1 >> 1;
            if (totalList.get(mid).getYear() >= year) r = mid;
            else l = mid + 1;
        }
        while (l < totalList.size() && totalList.get(l).getYear() == year) {
            for (String term : Paper.getAllTerms(totalList.get(l))) {
                ans.put(term, ans.getOrDefault(term, 0) + 1);
            }
            ++l;
        }
        return ans;
    }

    /**
     * 获取某一个作者在单个年份的最关注领域
     *
     * @param author : 作者
     * @param year   : 年份
     */
    public AttentionDTO queryMaxAttention(final Author author, final int year) {
        // 获取所有的领域名
        Set<String> fieldNames = Author.getAllFields(author);
        fieldNames.remove("");

        long maxScore = 0;
        AttentionDTO attentionDTO = new AttentionDTO();
        attentionDTO.setAuthor(author);
        attentionDTO.setYear(year);
        for (String fieldName : fieldNames) {
            long cnt = mongoTemplate.count(
                    new Query(Criteria.where("terms").regex(fieldName)
                            .and("authors").regex(author.getAuthorName())
                            .and("year").is(year)), Paper.class);
            if (cnt > maxScore) {
                Field field = mongoTemplate.findOne(Query.query(Criteria.where("fieldName").is(fieldName)), Field.class);
                if (field != null){
                    maxScore = cnt;
                    attentionDTO.setFieldName(fieldName);
                    attentionDTO.setScore(cnt);
                    attentionDTO.setFieldId(field.getId());
                }

            }
        }
        if (maxScore == 0) {
            attentionDTO.setScore(0);
            attentionDTO.setFieldName("");
        }
        return attentionDTO;
    }

    private List<Paper> filterPaper(List<Paper> totalList, int year) {
        return totalList.stream()
                .filter((Paper p) -> p.getYear() == year)
                .collect(Collectors.toList());
    }

    private int[] getMinMaxYear(List<Paper> totalList) {
        int min = Integer.MAX_VALUE, max = 0;
        for (Paper p : totalList) {
            min = Math.min(min, p.getYear());
            max = Math.max(max, p.getYear());
        }
        return new int[]{min, max};
    }

    private Field queryFieldByName(String fieldName) {
        return mongoTemplate.findOne(Query.query(Criteria.where("fieldName").is(fieldName)), Field.class);
    }

}
