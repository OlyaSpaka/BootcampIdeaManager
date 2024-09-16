package com.example.demo.mapper;

import com.example.demo.dto.IdeaDTO;
import com.example.demo.models.Category;
import com.example.demo.models.Idea;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IdeaMapper {
    CompetitionRepository competitionRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;

    public IdeaMapper(CompetitionRepository competitionRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Idea map(IdeaDTO ideaDTO) throws IllegalArgumentException, ParseException {
        if (ideaDTO == null) return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date createdAt = dateFormat.parse(ideaDTO.getCreatedAt());
//        ideaDTO.setCreatedAt(createdAt);

        Idea idea = new Idea(
                ideaDTO.getTitle(),
                ideaDTO.getDescription(),
                ideaDTO.getKeyFeatures(),
                ideaDTO.getReferenceLinks(),
                createdAt,
                ideaDTO.getPictures()
        );

        idea.setId(ideaDTO.getId());

        if (isUserPresent(ideaDTO.getUserId())){
            idea.setUser(userRepository.findById(ideaDTO.getUserId()).orElse(null)); //won't be null because check above
        } else throw new IllegalArgumentException("Idea is not assigned to a user or user's id is invalid");
        if (isCompetitionPresent(ideaDTO.getCompetitionId())){
            idea.setCompetition(competitionRepository.findById(ideaDTO.getCompetitionId()).orElse(null)); //won't be null because check above
        } else throw new IllegalArgumentException("Idea is not assigned to a competition or the competition's id is invalid");
        if (areCategoriesPresent(ideaDTO.getCategoryIds())){
            idea.setCategories(
                    ideaDTO.getCategoryIds()
                            .stream()
                            .map(categoryRepository::findById)
                            .map(Optional::get)
                            .collect(Collectors.toSet()) //won't be null because check above
            );
        } else throw new IllegalArgumentException("Idea is not assigned a single category or one of the category's ids is invalid");

        return idea;
    }
    IdeaDTO map(Idea idea) throws IllegalArgumentException {
        if (idea == null) return null;

        IdeaDTO ideaDTO = new IdeaDTO(
                idea.getTitle(),
                idea.getDescription(),
                idea.getKeyFeatures(),
                idea.getReferenceLinks(),
                idea.getCreatedAt().toString(),
                idea.getPictures()
        );

        ideaDTO.setId(idea.getId());

        // Set Competition and User IDs only if present
        if (idea.getCompetition() != null) {
            ideaDTO.setCompetitionId(idea.getCompetition().getId());
        } else throw new IllegalArgumentException("Idea is not assigned to a competition or the competition's id is invalid");

        if (idea.getUser() != null) {
            ideaDTO.setUserId(idea.getUser().getId());
        } else throw new IllegalArgumentException("Idea is not assigned to a user or user's id is invalid");

        if (idea.getCategories() != null && !idea.getCategories().isEmpty()) {
            ideaDTO.setCategoryIds(
                    idea.getCategories()
                            .stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet()));
        } else throw new IllegalArgumentException("Idea is not assigned a single category or one of the category's ids is invalid");

        return ideaDTO;
    }

    public boolean isCompetitionPresent(Integer id){
        return competitionRepository.existsById(id);
    }
    public boolean areCategoriesPresent(Set<Integer> categoryIdSet){
        for (Integer id : categoryIdSet){
            if (!categoryRepository.existsById(id)) return false;
        }
        return true;
    }
    public boolean isUserPresent(Integer id){
        return userRepository.existsById(id);
    }
}
