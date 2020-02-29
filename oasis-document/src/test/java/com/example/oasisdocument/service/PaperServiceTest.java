package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.exceptions.BadReqException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PaperServiceTest {
    @Autowired
    private PaperService paperService;

    //模糊搜索测试
    @Test
    public void queryTest1() {
        String key = "java";
        List<Paper> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
    }

    @Test
    public void queryTest2() {
        String key = "domain";
        List<Paper> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
    }

    @Test
    public void queryTest3() {
        String key = "java";
        List<Paper> papers = paperService.queryPaper(key, "all");
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
        //限定authors查询
        papers = paperService.queryPaper(key, "authors");
        assertNotNull(papers);
        assertEquals(0, papers.size());
    }

    @Test
    public void queryTest4() {
        List<String> fields = Arrays.asList("title", "conferences", "terms",
                "keywords", "authors", "affiliations");
        for (String f : fields) {
            List<Paper> papers = paperService.queryPaper("java",
                    f);
            assertNotNull(papers);
        }
    }

    //错误参数处理
    @Test(expected = BadReqException.class)
    public void queryTest5() {
        List<String> fields = Arrays.asList("title", "conference", "terms",
                "keywords", "authors", "affiliations");
        for (String f : fields) {
            List<Paper> papers = paperService.queryPaper("java",
                    f);
            assertNotNull(papers);
        }
    }

    @Test
    public void refineTest1() {
        List<Paper> papers = new LinkedList<>();
        List<String> refinements = Arrays.asList(new String[]{"author:John"});
        papers = paperService.queryPaperRefine(papers, refinements);
        assertNotNull(papers);
        assertEquals(0, papers.size());
    }
}
