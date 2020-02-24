package com.example.oasisdocument.model;

import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.repository.PaperRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PaperCrudTest {
    @Autowired
    private PaperRepository paperRepository;

    private List<BigInteger> ids = new LinkedList<>();

    @Test
    public void findTest() {
        List<Paper> papers = paperRepository.findAll();
    }

}
