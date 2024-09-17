package com.example.demo.services;

import com.example.demo.models.Competition;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
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
class IdeaServiceTest {

    @Autowired
    private IdeaService ideaService;

    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    private Idea idea;
    private Competition competition;
    private User user;

    @BeforeEach
    void setUp() {
        competition = competitionRepository.save(new Competition("Competition Name", "Description", new Date(), new Date(), 3));
        user = userRepository.save(new User("username", "email@example.com", "password"));
        // Setting up an Idea object for testing
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferenceLinks("Test references");

        user.addIdea(idea);
        competition.addIdea(idea);

        idea = ideaRepository.save(idea);
    }

    @Test
    void testAddNewIdea() {
        // Given
        Idea newIdea = new Idea();
        newIdea.setCreatedAt(new Date());
        newIdea.setTitle("New Idea");
        newIdea.setDescription("New description");

        user.addIdea(newIdea);
        competition.addIdea(newIdea);

        // When
        ideaService.addNewIdea(newIdea);

        // Then
        List<Idea> ideas = ideaRepository.findAll();
        assertThat(ideas).hasSize(2); // Because setUp() already added one idea
        assertThat(ideas).extracting(Idea::getTitle).contains("New Idea");
    }

//    @Test
//    void testDeleteIdeaWhenExists() {
//        ideaRepository.flush();
//        // Arrange
////        Idea ideaToDelete = new Idea();
////        ideaToDelete.setCreatedAt(new Date());
////        ideaToDelete.setTitle("Test Idea");
////        ideaToDelete.setDescription("Test description");
//
////        username.addIdea(ideaToDelete);
////        competition.addIdea(ideaToDelete);
////
////        ideaRepository.save(ideaToDelete);
//
//        List<Idea> ideasBefore = ideaRepository.findAll();
//        assertThat(ideasBefore).contains(idea);
//
//        // Act
//        ideaService.deleteIdea(idea.getId());
//
//        // Assert
//        List<Idea> ideasAfter = ideaRepository.findAll();
//        assertThat(ideasAfter).doesNotContain(idea);
//    }

    @Test
    void testDeleteIdeaWhenNotExists() {
        // When & Then
        assertThrows(IllegalStateException.class, () -> ideaService.deleteIdea(999)); // ID 999 doesn't exist
    }

    @Test
    void testUpdateName() {
        // When
        ideaService.updateName(idea.getId(), "Updated description", "Updated title", "Updated key features", "Updated references");

        // Then
        Idea updatedIdea = ideaRepository.findById(idea.getId()).orElseThrow();
        assertThat(updatedIdea.getTitle()).isEqualTo("Updated title");
        assertThat(updatedIdea.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void testShowUserIdea() {
        userRepository.save(user);
        ideaRepository.save(idea);

        // When
        List<Idea> userIdeas = ideaService.showUserIdea(user.getId());

        // Then
        assertThat(userIdeas).extracting(Idea::getTitle).contains("Test Idea");
    }

    @AfterEach
    void cleanUp() {
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
