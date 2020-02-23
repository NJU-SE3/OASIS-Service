package com.example.oasispaper.mapper;

import com.example.oasispaper.model.VO.AuthorVO;
import com.example.oasispaper.model.VO.PaperVO;
import com.example.oasispaper.repository.PaperRepository;
import com.example.oasispaper.service.PaperService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class QueryMapperTest {
    Logger logger = LoggerFactory.getLogger(QueryMapperTest.class);


    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private QueryMapper queryMapper;

    @Autowired
    private PaperService paperService;

    @Before
    public void init() {
        paperConstruct();
    }

    @Test
    public void mapQuery() {
        Page<PaperVO> papers = PageHelper.startPage(10, 10)
                .doSelectPage(() -> queryMapper.queryAll("wxj"));
        assertEquals(papers.size(), 1);
    }

    private void paperConstruct() {
        PaperVO VO = new PaperVO();
        VO.setTitle("I'm title");
        VO.setAbstra("I'm abstract");
        VO.setConference("IEEE");
        VO.setKeywords("I'm keywords");
        VO.setTerms("I'm terms");
        VO.setStartPage("0");
        VO.setEndPage("100");
        VO.setPdfLink("www.baidu.com");
        VO.setCitationCount(10);
        VO.setReferenceCount(20);
        VO.setYear(10000);
        List<AuthorVO> list = new LinkedList<>();
        AuthorVO inner =
                new AuthorVO();
        inner.setAffiliation("nju");
        inner.setName("wxj");
        list.add(inner);
        inner = new AuthorVO();
        inner.setAffiliation("pku");
        inner.setName("khellokk");
        list.add(inner);
        VO.setAuthors(list);
        //add paper
        paperService.insertPaperVO(VO);
    }
}
