package com.example.oasispaper.service;

import com.example.oasispaper.model.Author;
import com.example.oasispaper.repository.AuthorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorModelTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void run() {
        Author author = new Author();
        author.setId(3);
        authorRepository.save(author);

        List<Author> list = authorRepository.findAll();
        assert list.size() == 1;
    }
}
