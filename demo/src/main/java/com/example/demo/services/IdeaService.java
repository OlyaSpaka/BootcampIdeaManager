package com.example.demo.services;

import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;

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
        if (idea.getCompetition() != null) {
            idea.getCompetition().removeIdea(idea);
        }

        if (idea.getUser() != null) {
            idea.getUser().removeIdea(idea);
        }

        // Delete the Idea
        ideaRepository.deleteById(id);
    }

    @Transactional
    public void updateName(Integer id,
                           String description,
                           String title,
                           String keyFeatures,
                           String referenceLinks) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "account with Id " + id + " does not exist."));
        idea.setDescription(description);
        idea.setKeyFeatures(keyFeatures);
        idea.setTitle(title);
        idea.setReferenceLinks(referenceLinks);

    }

    public List<Idea> showUserIdea(Integer userId) {
        return ideaRepository.findByUserId(userId);
    }

    public List<Idea> getFormattedIdeas(String search) {
        List<Idea> ideas = searchIdeas(search);

        ideas.forEach(idea -> {
            if (idea.getCreatedAt() != null) {
                idea.setFormattedDate(formatDate(idea.getCreatedAt()));
            }
        });
        return ideas;
    }

    public Idea displayIdea(Integer id) {
        return ideaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Idea not found with id: " + id));
    }

    public List<Idea> displayIdeasByUser(User user) {
        List<Idea> ideas = ideaRepository.findByUserId(user.getId());
        if (ideas.isEmpty()) {
            throw new NoSuchElementException("No ideas found for user: " + user.getId());
        }
        ideas.forEach(idea -> {
            if (idea.getCreatedAt() != null) {
                idea.setFormattedDate(formatDate(idea.getCreatedAt()));
            }
        });
        return ideas;
    }

    public List<Idea> searchIdeas(String search) {
        if (search == null || search.trim().isEmpty()) {
            return ideaRepository.findAll();
        } else {
            return ideaRepository.searchIdeas(search.toLowerCase());
        }
    }

    private String formatDate(Date createdAt) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd.MM.yy");
        return dateFormatter.format(createdAt);
    }
}