package com.example.demo.repositories;

import com.example.demo.models.Bookmark;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    List<Bookmark> findByUserId(Integer userId);

    Optional<Bookmark> findByIdeaAndUser(Idea idea, User user);

    void deleteByIdeaIdAndUserId(Integer ideaId, Integer userId);
}
