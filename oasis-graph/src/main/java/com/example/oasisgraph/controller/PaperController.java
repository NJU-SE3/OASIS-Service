package com.example.oasisgraph.controller;

import com.example.oasisgraph.nodes.Author;
import com.example.oasisgraph.nodes.Paper;
import com.example.oasisgraph.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/paper")
public class PaperController {
	@Autowired
	private AuthorRepository authorRepository;

	@GetMapping("")
	public String hello() {
		Author author = new Author();
		author.setName("Mike");
		Paper p1 = new Paper();
		p1.setTitle("java");
		Paper p2 = new Paper();
		p2.setTitle("python");
		author.setPapers(Arrays.asList(p1, p2));
		authorRepository.save(author);

		return "hello world";
	}
}
