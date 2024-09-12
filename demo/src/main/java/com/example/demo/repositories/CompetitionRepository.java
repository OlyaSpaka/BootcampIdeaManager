package com.example.demo.repositories;

import com.example.demo.models.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
}
