package com.example.demo.services;

import com.example.demo.models.IdeaSelection;
import com.example.demo.repositories.IdeaSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdeaSelectionService {

    private final IdeaSelectionRepository ideaSelectionRepository;

    @Autowired
    public IdeaSelectionService(IdeaSelectionRepository ideaSelectionRepository) {
        this.ideaSelectionRepository = ideaSelectionRepository;
    }

    public void addIdeaSelection(IdeaSelection ideaSelection) {
        ideaSelectionRepository.save(ideaSelection);
    }

    public void deleteIdeaSelection(Integer id) {
        boolean exists = ideaSelectionRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("IdeaSelection with Id " + id + " does not exist");
        } else {
            ideaSelectionRepository.deleteById(id);
        }
    }
}
