package com.example.oasisdocument.dao;


import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.BaseService;
import com.example.oasisdocument.service.IntermeService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.oasisdocument.service.IntermeService.affCounterCollection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableQueryTest {
    private Logger logger = LoggerFactory.getLogger(TableQueryTest.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BaseService baseService;
    @Autowired
    private IntermeService intermeService;

    @Test
    public void twoTableQuery() {
        mongoTemplate.remove(Query.query(new Criteria("fieldName").is("Software")),Field.class);
    }
}
