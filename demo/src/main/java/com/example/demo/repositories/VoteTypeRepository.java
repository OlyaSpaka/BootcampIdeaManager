package com.example.demo.repositories;

import com.example.demo.models.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTypeRepository extends JpaRepository<VoteType, Integer> {
}
