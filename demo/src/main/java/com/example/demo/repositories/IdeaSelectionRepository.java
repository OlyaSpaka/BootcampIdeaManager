package com.example.demo.repositories;

import com.example.demo.models.IdeaSelection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaSelectionRepository extends JpaRepository<IdeaSelection, Integer> {
}
