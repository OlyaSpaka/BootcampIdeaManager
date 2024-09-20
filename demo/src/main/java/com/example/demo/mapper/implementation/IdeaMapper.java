package com.example.demo.mapper.implementation;

import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.interf.CategoryMapperInt;
import com.example.demo.mapper.interf.CompetitionMapperInt;
import com.example.demo.mapper.interf.IdeaMapperInt;
import com.example.demo.mapper.interf.UserMapperInt;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IdeaMapper implements IdeaMapperInt {
    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapperInt categoryMapper;
    private final CompetitionMapperInt competitionMapper;
    private final UserMapperInt userMapper;
    private final VoteMapper voteMapper;

    private IdeaMapper(CompetitionRepository competitionRepository, UserRepository userRepository, CategoryRepository categoryRepository,
                       @Lazy CategoryMapperInt categoryMapper, @Lazy CompetitionMapperInt competitionMapper, @Lazy UserMapperInt userMapper, VoteMapper voteMapper) {
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.competitionMapper = competitionMapper;
        this.userMapper = userMapper;
        this.voteMapper = voteMapper;
    }

    @Override
    public Idea map(InputIdeaDTO inputIdeaDTO) throws IllegalArgumentException, ParseException {
        if (inputIdeaDTO == null) return null;

        Date createdAt;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yy");
            createdAt = dateFormat.parse(inputIdeaDTO.getCreatedAt());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format for createdAt", e);
        }

        Idea idea = new Idea(
                inputIdeaDTO.getTitle(),
                inputIdeaDTO.getDescription(),
                inputIdeaDTO.getKeyFeatures(),
                inputIdeaDTO.getReferenceLinks(),
                createdAt,
                inputIdeaDTO.getPictures()
        );

        User user = userRepository.findById(inputIdeaDTO.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Idea is not assigned to a user or user's id is invalid"));
        idea.setUser(user);

        idea.setCompetition(
                competitionRepository.findById(inputIdeaDTO.getCompetition().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Idea is not assigned to a competition or the competition's id is invalid"))
        );

        // Extract category names from the DTO
        Set<String> categoryNames = inputIdeaDTO.getCategories()
                .stream()
                .map(InputCategoryDTO::getName)
                .collect(Collectors.toSet());

        idea.setCategories(new HashSet<>(categoryRepository.findAllByNameIn(categoryNames)));

        return idea;
    }
    public OutputIdeaDTO map(Idea idea) throws IllegalArgumentException {
        if (idea == null) return null;

        String formattedDate = idea.getCreatedAt().toString();

        if (idea.getCreatedAt() != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd.MM.yy");
            formattedDate = dateFormatter.format(idea.getCreatedAt());
        }

        OutputIdeaDTO outputIdeaDTO = new OutputIdeaDTO(
                idea.getTitle(),
                idea.getDescription(),
                idea.getKeyFeatures(),
                idea.getReferenceLinks(),
                formattedDate,
                idea.getPictures()
        );

        outputIdeaDTO.setId(idea.getId());

        outputIdeaDTO.setCompetition(competitionMapper.map(idea.getCompetition()));
        outputIdeaDTO.setUser(userMapper.map(idea.getUser()));
        outputIdeaDTO.setCategories(
                idea.getCategories()
                        .stream()
                        .map(categoryMapper::map)
                        .collect(Collectors.toSet())
        );
        outputIdeaDTO.setVotes(idea.getVotes().stream()
                .map(voteMapper::map).toList());

        return outputIdeaDTO;
    }
}
