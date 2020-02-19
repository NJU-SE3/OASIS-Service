package com.example.oasispaper.repository;

import com.example.oasispaper.model.AuthorConference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorConfRepo extends JpaRepository<AuthorConference, Integer> {
}
