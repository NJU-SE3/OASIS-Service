package com.example.oasispaper.repository;

import com.example.oasispaper.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Integer> {
	List<Conference> findByName(String name);
}
