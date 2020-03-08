package com.example.oasispaper.repository;

import com.example.oasispaper.model.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AffiliationRepository extends JpaRepository<Affiliation, Integer> {
	List<Affiliation> findByName(String name);
}
