package com.example.demo.VotePackage;

import com.example.demo.IdeaPackage.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Idea,Long> {
}