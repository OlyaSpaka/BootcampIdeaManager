package com.example.demo.services;

import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.implementation.IdeaMapper;
import com.example.demo.models.Category;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repositories.IdeaRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IdeaService {
    private final IdeaRepository ideaRepository;
    private final IdeaMapper ideaMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public IdeaService(IdeaRepository ideaRepository, IdeaMapper ideaMapper,
                       CategoryRepository categoryRepository) {
        this.ideaRepository = ideaRepository;
        this.ideaMapper = ideaMapper;
        this.categoryRepository = categoryRepository;
    }

    public void addNewIdea(Idea idea) {
        ideaRepository.save(idea);
    }

    public Integer addNewIdea(InputIdeaDTO inputIdeaDTO) throws IllegalArgumentException, ParseException {
        Idea idea = ideaMapper.map(inputIdeaDTO);
        idea = ideaRepository.save(idea);
        return idea.getId();
    }

    @Transactional
    public void deleteIdea(Integer id) throws IllegalStateException {
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
                           String keyFeatures,
                           String referenceLinks) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "account with Id " + id + " does not exist."));
        idea.setDescription(description);
        idea.setKeyFeatures(keyFeatures);
        idea.setTitle(title);
        idea.setReferenceLinks(referenceLinks);
    }

    @Transactional
    public void updateName(Integer id,
                           OutputIdeaDTO ideaDTO) throws IllegalStateException {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "account with Id " + id + " does not exist."));
        if (ideaDTO.getTitle() != null && !ideaDTO.getTitle().isEmpty()) {
            idea.setTitle(ideaDTO.getTitle());
        }
        if (ideaDTO.getDescription() != null && !ideaDTO.getDescription().isEmpty()) {
            idea.setDescription(ideaDTO.getDescription());
        }
        if (ideaDTO.getKeyFeatures() != null && !ideaDTO.getKeyFeatures().isEmpty()) {
            idea.setKeyFeatures(ideaDTO.getKeyFeatures());
        }
        if (ideaDTO.getReferenceLinks() != null && !ideaDTO.getReferenceLinks().isEmpty()) {
            idea.setReferenceLinks(ideaDTO.getReferenceLinks());
        }
        if (ideaDTO.getCategories() != null && !ideaDTO.getCategories().isEmpty()) {
            Set<Category> categories = categoryRepository.findAllByNameIn(ideaDTO
                    .getCategories()
                    .stream()
                    .map(OutputCategoryDTO::getName)
                    .collect(Collectors.toSet())
            );
            idea.setCategories(categories);
        }
        ideaRepository.save(idea);
    }

    public List<Idea> showUserIdea(Integer userId) {
        return ideaRepository.findByUserId(userId);
    }

    public List<OutputIdeaDTO> getFormattedIdeas(String search) {
        List<Idea> ideas = searchIdeas(search);
        List<OutputIdeaDTO> mappedIdeas = new ArrayList<>();
        for (Idea idea : ideas) {
            mappedIdeas.add(ideaMapper.map(idea));
        }
        return mappedIdeas;
    }

    public OutputIdeaDTO findById(Integer id) {
        Idea idea = ideaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Idea not found with id: " + id));

        return ideaMapper.map(idea);
    }

    public List<OutputIdeaDTO> displayIdeasByUser(User user) {
        List<OutputIdeaDTO> resultList = new ArrayList<>();
        List<Idea> ideas = ideaRepository.findByUserId(user.getId());
        if (ideas.isEmpty()) {
            throw new NoSuchElementException("No ideas found for user: " + user.getId());
        }
        ideas.forEach(idea -> {
            if (idea.getCreatedAt() != null) {
                idea.setFormattedDate(formatDate(idea.getCreatedAt()));
                resultList.add(ideaMapper.map(idea));
            }
        });
        return resultList;
    }

    private List<Idea> searchIdeas(String search) {
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