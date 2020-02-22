package com.example.oasispaper.mapper;

import com.example.oasispaper.model.Paper;
import com.example.oasispaper.model.VO.PaperQueryVO;
import com.example.oasispaper.repository.PaperRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PaperMapperTest {
    Logger logger = LoggerFactory.getLogger(PaperMapperTest.class);


    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperMapper paperMapper;

    @Before
    public void init() {
        Paper paper = new Paper();
        paper.setTitle("asdhelloaA");
        paper.setId(1);
        paperRepository.save(paper);

        paper.setTerms("helloaA");
        paper.setId(2);
        paperRepository.save(paper);
    }

    @Test
    public void mapQuery() {
        Page<PaperQueryVO> papers = PageHelper.startPage(2, 1).doSelectPage(() -> paperMapper.queryAll("hello"));
        assert papers.size() == 1;
    }
}
