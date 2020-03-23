package com.example.oasisdocument.service;

import com.example.oasisdocument.model.VO.PaperBriefVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PaperServiceTest {
    @Autowired
    private PaperService paperService;

    //模糊搜索测试
    @Test
    public void queryTest1() {
        String key = "java";
        List<PaperBriefVO> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
    }

    @Test
    public void queryTest2() {
        String key = "domain";
        List<PaperBriefVO> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
    }

    @Test
    public void queryTest3() {
        String key = "java";
        List<PaperBriefVO> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
        //限定authors查询
        papers = paperService.queryPaper(key, "authors");
        assertNotNull(papers);
        assertThat(papers.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    public void queryTest4() {
        List<String> fields = Arrays.asList("title", "conferences", "terms",
                "keywords", "authors", "affiliations");
        for (String f : fields) {
            List<PaperBriefVO> papers = paperService.queryPaper("java",
                    f);
            assertNotNull(papers);
        }
    }

    @Test
    public void refineTest1() {
        List<PaperBriefVO> papers = new LinkedList<>();
        List<String> refinements = Arrays.asList(new String[]{"author:John"});
        papers = paperService.queryPaperRefine(papers, refinements);
        assertNotNull(papers);
        assertEquals(0, papers.size());
    }
}


