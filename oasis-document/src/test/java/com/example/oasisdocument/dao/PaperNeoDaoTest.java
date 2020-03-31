package com.example.oasisdocument.dao;

import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.repository.graph.AuthorNeoRepo;
import com.example.oasisdocument.repository.graph.PaperNeoRepo;
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
@ActiveProfiles("dev")
public class PaperNeoDaoTest {
	private Logger logger = LoggerFactory.getLogger(PaperNeoDaoTest.class);
	@Autowired
	private PaperNeoRepo paperNeoRepo;
	@Autowired
	private AuthorNeoRepo authorNeoRepo;

	@Test
	public void run() {
		AuthorNeo nep = authorNeoRepo.findByID(37295488600L);
		nep = authorNeoRepo.findAuthorNeoByAuthorName("Christine D. Piatko");
		System.out.println();
	}
}
