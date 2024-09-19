package com.example.demo.repositories;

import com.example.demo.models.Comment;
import com.example.demo.models.IdeaSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaSelectionRepository extends JpaRepository<IdeaSelection, Integer> {
    List<IdeaSelection> findByCompetitionId(Integer competitionId);

}
