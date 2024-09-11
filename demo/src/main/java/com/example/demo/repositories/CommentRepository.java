package com.example.demo.repositories;

import com.example.demo.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByIdeaId(Integer ideaId);
}
