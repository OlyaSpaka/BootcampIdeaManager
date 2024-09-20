package com.example.demo.services;

import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.mapper.implementation.IdeaMapper;
import com.example.demo.models.Category;
import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.IdeaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IdeaServiceTest {
    @Mock
    private IdeaRepository ideaRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private IdeaMapper ideaMapper;
    @InjectMocks
    private IdeaService ideaService;
    private Idea idea;
    private OutputIdeaDTO outputIdeaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idea = new Idea();
        idea.setId(1);
        idea.setTitle("Original Title");
        idea.setDescription("Original Description");
        idea.setKeyFeatures("Original Key Features");
        idea.setReferenceLinks("Original Reference Links");

        outputIdeaDTO = new OutputIdeaDTO();
        outputIdeaDTO.setTitle("Updated Title");
        outputIdeaDTO.setDescription("Updated Description");
        outputIdeaDTO.setKeyFeatures("Updated Key Features");
        outputIdeaDTO.setReferenceLinks("Updated Reference Links");

        OutputCategoryDTO categoryDTO = new OutputCategoryDTO();
        categoryDTO.setName("Updated Category");

        outputIdeaDTO.setCategories(Set.of(categoryDTO));

        outputIdeaDTO.setPictures("Updated Pictures");
    }

    @Test
    void testAddNewIdea() throws ParseException {
        // Arrange
        InputIdeaDTO inputIdeaDTO = new InputIdeaDTO();
        inputIdeaDTO.setTitle("New Idea");
        inputIdeaDTO.setDescription("New description");

        Idea mappedIdea = new Idea();
        mappedIdea.setTitle(inputIdeaDTO.getTitle());
        mappedIdea.setDescription(inputIdeaDTO.getDescription());

        Idea savedIdea = new Idea();
        savedIdea.setId(1);

        when(ideaMapper.map(inputIdeaDTO)).thenReturn(mappedIdea);
        when(ideaRepository.save(any(Idea.class))).thenReturn(savedIdea);

        // Act
        Integer ideaId = ideaService.addNewIdea(inputIdeaDTO);

        // Assert
        verify(ideaMapper, times(1)).map(inputIdeaDTO);
        verify(ideaRepository, times(1)).save(mappedIdea);
        assertEquals(savedIdea.getId(), ideaId);
    }
    @Test
    void testDeleteIdea() {
        // Arrange
        Idea existingIdea = new Idea();
        existingIdea.setId(1);
        existingIdea.setUser(new User());
        existingIdea.setCompetition(new Competition());

        when(ideaRepository.findById(1)).thenReturn(Optional.of(existingIdea));

        // Act
        ideaService.deleteIdea(1);

        // Assert
        verify(ideaRepository, times(1)).findById(1);
        verify(ideaRepository, times(1)).deleteById(1);
        assertNull(existingIdea.getUser());
        assertNull(existingIdea.getCompetition());
    }
    @Test
    void testDeleteIdeaNotFound() {
        // Mock repository behavior for non-existent idea
        when(ideaRepository.findById(999)).thenReturn(Optional.empty());

        // Verify that an exception is thrown
        assertThrows(IllegalStateException.class, () -> ideaService.deleteIdea(999));

        verify(ideaRepository, times(1)).findById(999);
        verify(ideaRepository, times(0)).save(any(Idea.class)); // Save should not be called
    }

    @Test
    void testUpdateIdeaNotFound() {
        // Mock repository behavior for non-existent idea
        when(ideaRepository.findById(999)).thenReturn(Optional.empty());

        // Verify that an exception is thrown
        assertThrows(IllegalStateException.class, () -> ideaService.updateIdea(999, outputIdeaDTO));

        verify(ideaRepository, times(1)).findById(999);
        verify(ideaRepository, times(0)).save(any(Idea.class)); // Save should not be called
    }

    @Test
    void testUpdateIdea() {
        // Mock repository behavior
        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea));
        when(categoryRepository.findAllByNameIn(any())).thenReturn(
                Set.of(new Category("Updated Category")));

        // Act
        ideaService.updateIdea(1, outputIdeaDTO);

        // Verify repository calls
        verify(ideaRepository, times(1)).findById(1);
        verify(categoryRepository, times(1)).findAllByNameIn(
                outputIdeaDTO.getCategories()
                        .stream()
                        .map(OutputCategoryDTO::getName)
                        .collect(Collectors.toSet()));
        verify(ideaRepository, times(1)).save(idea);

        // Verify that the entity was updated correctly
        assert (idea.getTitle().equals("Updated Title"));
        assert (idea.getDescription().equals("Updated Description"));
        assert (idea.getKeyFeatures().equals("Updated Key Features"));
        assert (idea.getReferenceLinks().equals("Updated Reference Links"));
        assert (idea.getPictures().equals("Updated Pictures"));
        assert (idea.getCategories().size() == 1);
    }
    @Test
    void testGetFormattedIdeas() {
        // Arrange
        String search = "test";
        Idea idea = new Idea();
        idea.setTitle("Test Idea");

        List<Idea> ideas = Collections.singletonList(idea);
        when(ideaRepository.searchIdeas(search.toLowerCase())).thenReturn(ideas);
        when(ideaMapper.map(idea)).thenReturn(new OutputIdeaDTO());

        // Act
        List<OutputIdeaDTO> formattedIdeas = ideaService.getFormattedIdeas(search);

        // Assert
        verify(ideaRepository, times(1)).searchIdeas(search.toLowerCase());
        verify(ideaMapper, times(1)).map(idea);
        assertEquals(1, formattedIdeas.size());
    }
    @Test
    void testFindById() {
        // Arrange
        Idea idea = new Idea();
        idea.setId(1);
        idea.setTitle("Test Idea");

        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea));
        when(ideaMapper.map(idea)).thenReturn(new OutputIdeaDTO());

        // Act
        OutputIdeaDTO result = ideaService.findById(1);

        // Assert
        verify(ideaRepository, times(1)).findById(1);
        verify(ideaMapper, times(1)).map(idea);
    }

    @Test
    void testDisplayIdeasByUser() {
        // Arrange
        User user = new User();
        user.setId(1);

        Idea idea = new Idea();
        idea.setUser(user);
        idea.setCreatedAt(new Date());

        when(ideaRepository.findByUserId(1)).thenReturn(Collections.singletonList(idea));
        when(ideaMapper.map(idea)).thenReturn(new OutputIdeaDTO());

        // Act
        List<OutputIdeaDTO> result = ideaService.displayIdeasByUser(user);

        // Assert
        assertEquals(1, result.size());
        verify(ideaRepository, times(1)).findByUserId(1);
        verify(ideaMapper, times(1)).map(idea);
    }

    @Test
    void testFindSelectedIdeas() {
        // Arrange
        Idea idea = new Idea();
        idea.setCreatedAt(new Date());

        when(ideaRepository.findSelectedIdeas()).thenReturn(Collections.singletonList(idea));
        when(ideaMapper.map(idea)).thenReturn(new OutputIdeaDTO());

        // Act
        List<OutputIdeaDTO> result = ideaService.findSelectedIdeas();

        // Assert
        assertEquals(1, result.size());
        verify(ideaRepository, times(1)).findSelectedIdeas();
        verify(ideaMapper, times(1)).map(idea);
    }

    @Test
    void testRemovePictures() {
        // Arrange
        Idea idea = new Idea();
        idea.setId(1);
        idea.setPictures("pic1.jpg,pic2.jpg");

        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea));

        // Act
        ideaService.removePictures(1, Collections.singletonList("pic2.jpg"));

        // Assert
        verify(ideaRepository, times(1)).save(idea);
        assertEquals("pic1.jpg", idea.getPictures());
    }
}
