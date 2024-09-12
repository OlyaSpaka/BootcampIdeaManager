package com.example.demo.repositories;

import com.example.demo.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByIdeaId(Integer ideaId);
}