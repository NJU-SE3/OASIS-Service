package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Paper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PaperServiceTest {
    @Autowired
    private PaperService paperService;

    @Test
    public void queryTest() {
        String key = "java";
//        List<Paper> papers = paperService.queryPaper(key, "all");
//        assertEquals(1, papers.size());
    }

    @Test
    public void edgeTest1() {
        String key = "java";
//        List<Paper> papers = paperService.queryPaper(key, "all");

    }
}
