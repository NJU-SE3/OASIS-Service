package com.example.oasisdocument.service;

import com.example.oasisdocument.exceptions.BadReqException;
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

    /**
     * 作者在某一个领域内的关注度趋势 , 按照年份进行查找
     */
    public List<AttentionDTO> queryAttentionTrend(final String authorId, final String fieldName) {
        Author author = mongoTemplate.findById(authorId, Author.class);
        if (null == author) {
            throw new EntityNotFoundException();
        }
        //获取作者关联论文列表
        List<Paper> papers = mongoTemplate.find(new Query(Criteria.where("authors").regex(author.getAuthorName()).and("terms").regex(fieldName)),
                Paper.class);
        List<AttentionDTO> ans = new LinkedList<>();
        // 按照年份排序
        papers.sort(Comparator.comparingInt(Paper::getYear));
        if (papers.isEmpty()) return ans;
        int minYear = papers.get(0).getYear(), maxYear = papers.get(papers.size() - 1).getYear();
        for (int year = minYear; year <= maxYear; ++year) {
            AttentionDTO attentionDTO = new AttentionDTO();
            attentionDTO.setAuthor(author);
            attentionDTO.setYear(year);
            attentionDTO.setFieldName(fieldName);
            // 获取得分
            long cnt = mongoTemplate.count(
                    new Query(Criteria.where("authors")
                            .regex(author.getAuthorName())
                            .and("terms").regex(fieldName)
                            .and("year").is(year)),
                    Paper.class);
            attentionDTO.setScore(cnt);
        }

        return ans;
    }

    /**
     * 批量获取最关注领域
     */
    public List<AttentionDTO> batchQueryMaxAttention(final String authorId, final int minYear, final int maxYear) {
        List<AttentionDTO> res = new LinkedList<>();
        for (int y = minYear; y <= maxYear; ++y) {
            res.add(queryMaxAttention(authorId, y));
        }
        return res;
    }

    /**
     * 获取某一个作者在单个年份的最关注领域
     *
     * @param authorId: 作者ID
     * @param year      : 年份
     */
    public AttentionDTO queryMaxAttention(final String authorId, final int year) {
        Author author = mongoTemplate.findById(authorId, Author.class);
        if (null == author) {
            throw new EntityNotFoundException();
        }
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
                maxScore = cnt;
                attentionDTO.setFieldName(fieldName);
                attentionDTO.setScore(cnt);
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

}
