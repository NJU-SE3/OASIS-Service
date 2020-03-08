package com.example.oasispaper.repository;

import com.example.oasispaper.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	List<Author> findAllByName(String name);
}
