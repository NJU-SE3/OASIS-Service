package com.example.oasispaper.repository;

import com.example.oasispaper.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorConfRepo extends JpaRepository<Publication, Integer> {
}
