package com.example.demo.repositories;

import com.example.demo.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
}
