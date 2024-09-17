package com.example.demo.repositories;

import com.example.demo.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    List<Bookmark> findByUserId(Integer userId);
    void deleteByIdeaIdAndUserId(Integer ideaId, Integer userId);
}
