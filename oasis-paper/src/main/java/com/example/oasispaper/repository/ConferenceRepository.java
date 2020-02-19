package com.example.oasispaper.repository;

import com.example.oasispaper.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Integer> {
}
