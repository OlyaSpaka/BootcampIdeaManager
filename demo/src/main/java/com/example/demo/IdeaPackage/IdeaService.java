package com.example.demo.IdeaPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteIdea(Long id) {
        boolean exists = ideaRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Idea with Id " + id + " does not exist");
        } else {
            ideaRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateIdea(Long id,
                           String description,
                           String title,
                           String key_features,
                           String references) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "idea with Id " + id + " does not exist."));
      idea.setDescription(description);
      idea.setKey_features(key_features);
      idea.setTitle(title);
      idea.setReferences(references);

    }
}