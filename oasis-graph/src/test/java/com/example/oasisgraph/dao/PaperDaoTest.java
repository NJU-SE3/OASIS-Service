package com.example.oasisgraph.dao;


import com.example.oasisgraph.nodes.Paper;
import com.example.oasisgraph.repository.PaperRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaperDaoTest {
	@Autowired
	private PaperRepository paperRepository;

	@Test
	public void baseTest1() {
		Paper person = new Paper();
		person = paperRepository.save(person);
		assertNotNull(person);
		assertNotNull(person.getId());

		Long personId = person.getId();
		Optional<Paper> op = paperRepository.findById(personId);
		assertTrue(op.isPresent());
		paperRepository.delete(person);
	}

}
