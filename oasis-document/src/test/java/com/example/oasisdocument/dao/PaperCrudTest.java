package com.example.oasisdocument.dao;

import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.repository.docs.PaperRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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
        List<Paper> papers = paperRepository.findAllById("312");
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

    private void run() {
        ReentrantLock lock = new ReentrantLock();

    }
}
