package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.options.AttentionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中间数据
 */
@Service
public class IntermeService {
    private static final Logger logger = LoggerFactory.getLogger(IntermeService.class);
    public static final String affCounterCollection = "aff_counter";
    public static final String authorCounterCollection = "author_counter";
    public static final String fieldCounterCollection = "field_counter";
    public static final String conferenceCounterCollection = "conference_counter";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Async
    public void genAffCounter() {
        final String colName = affCounterCollection;

        //建立document
        if (!intermeExist(colName))
            mongoTemplate.createCollection(colName);
        List<Affiliation> affiliationList = mongoTemplate.findAll(Affiliation.class);
        for (Affiliation en : affiliationList) {
            CounterBaseEntity entity = findViaCheckId(en.getId());
            if (null == entity) continue;
            //幂等保障
            if (mongoTemplate.exists(new Query(Criteria.where("id").is(en.getId())), colName))
                continue;
            mongoTemplate.save(entity, colName);
        }
        logger.info("affiliation counter rank generate success");
    }

    @Async
    public void genAuthorCounter() {
        final String colName = authorCounterCollection;
        //建立document
        if (!intermeExist(colName))
            mongoTemplate.createCollection(colName);
        List<Author> authorList = mongoTemplate.findAll(Author.class);
        for (Author en : authorList) {
            CounterBaseEntity entity = findViaCheckId(en.getId());
            if (null == entity) continue;
            if (mongoTemplate.exists(new Query(Criteria.where("id").is(en.getId())), colName))
                continue;
            mongoTemplate.save(entity, colName);
        }
        logger.info("author counter rank generate success");
    }

    @Async
    public void genFiledCounter() {
        final String colName = fieldCounterCollection;
        if (!intermeExist(colName))
            mongoTemplate.createCollection(colName);
        List<com.example.oasisdocument.model.docs.extendDoc.Field> fieldList =
                mongoTemplate.findAll(com.example.oasisdocument.model.docs.extendDoc.Field.class);
        for (com.example.oasisdocument.model.docs.extendDoc.Field en : fieldList) {
            CounterBaseEntity entity = findViaCheckId(en.getId());
            if (null == entity) continue;
            if (mongoTemplate.exists(new Query(Criteria.where("id").is(en.getId())), colName))
                continue;
            mongoTemplate.save(entity, colName);
        }
        logger.info("field counter rank generate success");

    }

    @Async
    public void genConferenceCounter() {
        final String colName = conferenceCounterCollection;
        if (!intermeExist(colName))
            mongoTemplate.createCollection(colName);
        List<Conference> conferenceList =
                mongoTemplate.findAll(Conference.class);
        for (Conference en : conferenceList) {
            CounterBaseEntity entity = findViaCheckId(en.getId());
            if (null == entity) continue;
            if (mongoTemplate.exists(new Query(Criteria.where("id").is(en.getId())), colName))
                continue;
            mongoTemplate.save(entity, colName);

        }
        logger.info("conference counter rank generate success");
    }


    public boolean intermeExist(String colName) {
        return mongoTemplate.collectionExists(colName);
    }

    private CounterBaseEntity findViaCheckId(String checkId) {
        return mongoTemplate
                .findOne(Query.query(
                        Criteria.where("checkId").is(checkId).and("year").is(-1)),
                        CounterBaseEntity.class);
    }

}
