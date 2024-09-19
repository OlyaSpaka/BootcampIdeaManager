package com.example.demo.repositories;

import com.example.demo.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByIdeaId(Integer ideaId);
    Optional<Vote> findByUserIdAndIdeaId(Integer userId, Integer ideaId);
    List<Vote> findByUserId(Integer ideaId);
}