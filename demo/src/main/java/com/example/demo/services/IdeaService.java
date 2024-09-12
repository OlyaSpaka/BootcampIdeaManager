package com.example.demo.services;

import com.example.demo.models.Idea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repositories.IdeaRepository;

import java.util.List;

@Service
public class IdeaService {
    private final IdeaRepository ideaRepository;

    @Autowired
    public IdeaService(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public void addNewIdea(Idea idea) {
        ideaRepository.save(idea);
    }

    @Transactional
    public void deleteIdea(Integer id) {
        Idea idea = ideaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Idea with Id " + id + " does not exist"));

        // Remove the Idea from User and Competition
        if (idea.getUser() != null) {
            idea.getUser().removeIdea(idea);
        }

        if (idea.getCompetition() != null) {
            idea.getCompetition().removeIdea(idea);
        }

        // Delete the Idea
        ideaRepository.deleteById(id);
    }

    @Transactional
    public void updateName(Integer id,
                           String description,
                           String title,
                           String key_features,
                           String references) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "account with Id " + id + " does not exist."));
      idea.setDescription(description);
      idea.setKeyFeatures(key_features);
      idea.setTitle(title);
      idea.setReferences(references);

    }

    public List<Idea> showUserIdea(Integer userId){
        return ideaRepository.findByUserId(userId);
    }
}