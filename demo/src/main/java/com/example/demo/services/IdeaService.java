package com.example.demo.services;

import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.implementation.IdeaMapper;
import com.example.demo.models.Category;
import com.example.demo.models.Idea;
import com.example.demo.models.IdeaSelection;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repositories.IdeaRepository;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IdeaService {
    private final IdeaRepository ideaRepository;
    private final IdeaSelectionService ideaSelectionService;
    private final IdeaMapper ideaMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public IdeaService(IdeaRepository ideaRepository, IdeaSelectionService ideaSelectionService) {
    public IdeaService(IdeaRepository ideaRepository, IdeaMapper ideaMapper,
                       CategoryRepository categoryRepository) {
        this.ideaRepository = ideaRepository;
        this.ideaSelectionService = ideaSelectionService;
        this.ideaMapper = ideaMapper;
        this.categoryRepository = categoryRepository;
    }

//    public void addNewIdea(Idea idea) {
//        ideaRepository.save(idea);
//    }

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
    public void updateIdea(Integer id,
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
//        if (ideaDTO.getPictures() != null && !ideaDTO.getPictures().isEmpty()) {
            idea.setPictures(ideaDTO.getPictures());
//        }
        ideaRepository.save(idea);
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

    public void removePictures(Integer ideaId, List<String> removePictures) {
        // Fetch idea, modify the list of pictures, and save the idea
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new IllegalStateException("Idea with id " + ideaId + " does not exist"));

        List<String> updatedPictures = Arrays.stream(idea.getPictures().split(","))
                .filter(picture -> !removePictures.contains(picture))
                .collect(Collectors.toList());

        idea.setPictures(String.join(",", updatedPictures));
        ideaRepository.save(idea);
    }

    private List<Idea> searchIdeas(String search) {
        if (search == null || search.trim().isEmpty()) {
            return ideaRepository.findAll();
        } else {
            return ideaRepository.searchIdeas(search.toLowerCase());
        }
    }
    public List<Idea> getSelectedIdeas(Integer competitionId){
        List<Idea> selectedIdeasFull = new ArrayList<>();
        List<IdeaSelection> selectedIdeas = ideaSelectionService.getSelectedIdeas(competitionId);

        for(IdeaSelection ideaSelection : selectedIdeas){
            for(Idea idea : ideaRepository.findAll()){
                if(Objects.equals(idea.getId(), ideaSelection.getId())){
                    selectedIdeasFull.add(idea);
                }
            }
        }
        return selectedIdeasFull;
    }

    private String formatDate(Date createdAt) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd.MM.yy");
        return dateFormatter.format(createdAt);
    }
}