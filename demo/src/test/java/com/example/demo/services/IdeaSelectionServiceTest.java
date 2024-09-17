package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.IdeaSelection;
import com.example.demo.models.User;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.IdeaSelectionRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class IdeaSelectionServiceTest {
    @Autowired
    private IdeaSelectionService ideaSelectionService;
    @Autowired
    private IdeaSelectionRepository ideaSelectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    IdeaSelection ideaSelection;
    User ideaUser;
    Competition competition;
    Idea idea;


    @BeforeEach
    void setUp() {
        ideaUser = userRepository.save(new User("ideaUsername", "email123@example.com", "password123"));
        competition = competitionRepository.save(new Competition("title", "description", new Date(), new Date(),3));
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferenceLinks("Test references");
        ideaUser.addIdea(idea);
        competition.addIdea(idea);
        idea = ideaRepository.save(idea);
        ideaSelection = new IdeaSelection();
        ideaSelection.setIdea(idea);
        ideaSelection.setCompetition(competition);
        ideaSelection.setDate(new Date());

        ideaSelectionRepository.save(ideaSelection);
    }

    @Test
    void addNewIdeaSelection() {
        Idea testIdea = new Idea();
        testIdea.setCreatedAt(new Date());
        testIdea.setTitle("Test Idea");
        testIdea.setDescription("Test description");
        testIdea.setKeyFeatures("Test features");
        testIdea.setReferenceLinks("Test references");
        ideaUser.addIdea(testIdea);
        competition.addIdea(testIdea);
        testIdea = ideaRepository.save(testIdea);
        IdeaSelection testIdeaSelection = new IdeaSelection();
        testIdeaSelection.setIdea(testIdea);
        testIdeaSelection.setCompetition(competition);
        testIdeaSelection.setDate(new Date());

        ideaSelectionService.addIdeaSelection(testIdeaSelection);

        List<IdeaSelection> ideaSelectionListAfter = ideaSelectionRepository.findAll();
        assertThat(ideaSelectionListAfter).hasSize(2);
    }

    @Test
    void testDeleteIdeaSelectionWhenExists() {
        Idea testIdea = new Idea();
        testIdea.setCreatedAt(new Date());
        testIdea.setTitle("Test Idea");
        testIdea.setDescription("Test description");
        testIdea.setKeyFeatures("Test features");
        testIdea.setReferenceLinks("Test references");
        ideaUser.addIdea(testIdea);
        competition.addIdea(testIdea);
        testIdea = ideaRepository.save(testIdea);
        IdeaSelection ideaSelectionToDelete = new IdeaSelection();
        ideaSelectionToDelete.setIdea(testIdea);
        ideaSelectionToDelete.setCompetition(competition);
        ideaSelectionToDelete.setDate(new Date());

        ideaSelectionRepository.save(ideaSelectionToDelete);

        // Act
        ideaSelectionService.deleteIdeaSelection(ideaSelectionToDelete.getId());

        // Assert
        List<IdeaSelection> ideaSelectionAfter = ideaSelectionRepository.findAll();
        assertThat(ideaSelectionAfter).doesNotContain(ideaSelectionToDelete);
    }

    @Test
    void testDeleteIdeaSelectionWhenNotExists() {
        assertThrows(IllegalStateException.class, () -> ideaSelectionService.deleteIdeaSelection(999));
    }

    @AfterEach
    void cleanUp() {
        ideaSelectionRepository.deleteAll();
        ideaRepository.deleteAll();


        competitionRepository.deleteAll();
        userRepository.deleteAll();


    }
}
