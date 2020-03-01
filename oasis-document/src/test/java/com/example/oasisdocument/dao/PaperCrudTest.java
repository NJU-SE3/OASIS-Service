package com.example.oasisdocument.dao;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.repository.PaperRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PaperCrudTest {
    @Autowired
    private PaperRepository paperRepository;

    @Test
    public void findTest() {
        List<Paper> papers = paperRepository.findAll();
        assertNotEquals(null, papers);
    }

    @Test
    public void findByIdTest() {
        List<Paper> papers = paperRepository.findAllById(BigInteger.valueOf(312));
        assertNotEquals(null, papers);
    }

    @Test
    public void findByYearTest1() {
        List<Paper> papers = paperRepository.findAllByYear(2017);
        assertNotNull(papers);
        assertNotEquals(0, papers.size());
    }

    @Test
    public void findByYearTest2() {
        List<Paper> papers = paperRepository.findAllByYear(4032);
        assertNotNull(papers);
        assertEquals(0, papers.size());
    }
}
