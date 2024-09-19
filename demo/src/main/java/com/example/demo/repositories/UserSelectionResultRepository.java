package com.example.demo.repositories;

import com.example.demo.models.UserSelectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSelectionResultRepository extends JpaRepository<UserSelectionResult, Integer> {
}