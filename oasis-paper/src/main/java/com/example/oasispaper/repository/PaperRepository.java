package com.example.oasispaper.repository;

import com.example.oasispaper.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Integer> {
}
