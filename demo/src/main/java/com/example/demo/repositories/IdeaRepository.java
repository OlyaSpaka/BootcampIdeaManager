package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Integer> {
    List<Idea> findByUserId(Integer userId);
}
