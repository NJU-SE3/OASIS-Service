package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Cacheable(cacheNames = "paperQuery", unless = "#result==null")
    public List<Paper> queryPaper(String key, int pageNum, int pageSize) {
        Pageable pageable =
                PageRequest.of(pageNum, pageSize);
        Query query = new Query(Paper.getQueryCriteria(key));
        query.with(pageable);
        return mongoTemplate.find(query, Paper.class);
    }

    @Override
    public void insert(Paper entity) {
        paperRepository.save(entity);
    }
}
