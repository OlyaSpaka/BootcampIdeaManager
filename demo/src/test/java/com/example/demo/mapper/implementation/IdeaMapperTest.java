package com.example.demo.mapper.implementation;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.mapper.interf.CategoryMapperInt;
import com.example.demo.mapper.interf.CompetitionMapperInt;
import com.example.demo.mapper.interf.UserMapperInt;
import com.example.demo.models.Category;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.models.Competition;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdeaMapperTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapperInt categoryMapper;

    @Mock
    private CompetitionMapperInt competitionMapper;

    @Mock
    private UserMapperInt userMapper;

    @InjectMocks
    private IdeaMapper ideaMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapInputIdeaDTOToIdea() throws Exception {
        InputIdeaDTO inputDTO = new InputIdeaDTO();
        inputDTO.setTitle("New Idea");
        inputDTO.setDescription("Description of the new idea");
        inputDTO.setKeyFeatures("Feature1");
        inputDTO.setReferenceLinks("https://example.com");
        inputDTO.setCreatedAt("14:00 19.09.24");
        inputDTO.setPictures("picture.png");
        CompetitionDTO competitionDTO = new CompetitionDTO();
        competitionDTO.setId(1);
        inputDTO.setCompetition(competitionDTO);
        OutputUserDTO userDto = new OutputUserDTO();
        userDto.setId(1);
        inputDTO.setUser(userDto);
        inputDTO.setCategories(Set.of(new InputCategoryDTO("category1"), new InputCategoryDTO("category2")));

        User user = new User();
        user.setId(1);

        Competition competition = new Competition();
        competition.setId(1);

        Category category1 = new Category();
        category1.setId(1);

        Category category2 = new Category();
        category2.setId(2);

        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));
        when(competitionRepository.findById(1)).thenReturn(java.util.Optional.of(competition));
        when(categoryRepository.findById(1)).thenReturn(java.util.Optional.of(category1));
        when(categoryRepository.findById(2)).thenReturn(java.util.Optional.of(category2));
        Set<Category> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);
        when(categoryRepository.findAllByNameIn(any())).thenReturn(categories);
        when(categoryRepository.findById(2)).thenReturn(java.util.Optional.of(category2));

        Idea idea = ideaMapper.map(inputDTO);

        assertNotNull(idea);
        assertEquals("New Idea", idea.getTitle());
        assertEquals("Description of the new idea", idea.getDescription());
        assertEquals("Feature1", idea.getKeyFeatures());
        assertEquals("https://example.com", idea.getReferenceLinks());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2024-09-19T12:00:00Z"), idea.getCreatedAt());
        assertEquals("picture.png", idea.getPictures());
        assertEquals(user, idea.getUser());
        assertEquals(competition, idea.getCompetition());
        assertTrue(idea.getCategories().contains(category1));
        assertTrue(idea.getCategories().contains(category2));
    }

    @Test
    void testMapIdeaToOutputIdeaDTO() throws ParseException {
        Idea idea = new Idea();
        idea.setId(1);
        idea.setTitle("Innovative Idea");
        idea.setDescription("A new approach to solving problems");
        idea.setKeyFeatures("Feature1, Feature2");
        idea.setReferenceLinks("https://example.com");
        idea.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2024-09-19T12:00:00Z"));
        idea.setPictures("picture.png");

        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");

        Competition competition = new Competition();
        competition.setId(1);
        competition.setName("Innovation Contest");

        Category category1 = new Category();
        category1.setId(1);
        Category category2 = new Category();
        category2.setId(2);

        idea.setUser(user);
        idea.setCompetition(competition);
        idea.setCategories(Set.of(category1, category2));

        OutputUserDTO userDTO = new OutputUserDTO();
        userDTO.setId(1);
        userDTO.setUsername("john_doe");
        when(userMapper.map(any(User.class))).thenReturn(userDTO);
        CompetitionDTO competitionDTO = new CompetitionDTO();
        competitionDTO.setId(1);
        competitionDTO.setName("Innovation Contest");
        when(competitionMapper.map(any(Competition.class))).thenReturn(competitionDTO);
        OutputCategoryDTO categoryDTO1 = new OutputCategoryDTO();
        categoryDTO1.setId(1);
        OutputCategoryDTO categoryDTO2 = new OutputCategoryDTO();
        categoryDTO1.setId(2);
        when(categoryMapper.map(category1)).thenReturn(categoryDTO1);
        when(categoryMapper.map(category2)).thenReturn(categoryDTO2);

        OutputIdeaDTO dto = ideaMapper.map(idea);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Innovative Idea", dto.getTitle());
        assertEquals("A new approach to solving problems", dto.getDescription());
        assertEquals("Feature1, Feature2", dto.getKeyFeatures());
        assertEquals("https://example.com", dto.getReferenceLinks());
        assertEquals("14:00 19.09.24", dto.getCreatedAt());
        assertEquals("picture.png", dto.getPictures());
        assertEquals(1, dto.getUser().getId());
        assertEquals("john_doe", dto.getUser().getUsername());
        assertEquals(1, dto.getCompetition().getId());
        assertEquals("Innovation Contest", dto.getCompetition().getName());
    }
}
