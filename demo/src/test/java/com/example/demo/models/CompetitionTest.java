package com.example.demo.models;

import com.example.demo.repositories.CompetitionRepository;
import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompetitionTest {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private Competition savedCompetition;
    private Idea idea;
    private User user;

    @BeforeEach
    @Transactional
    void setUp() {
        // Setup initial data
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,10,1);
        savedCompetition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        user = userRepository.save(new User("username", "email@example.com", "password"));
        idea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        savedCompetition.addIdea(idea);
        user.addIdea(idea);

        entityManager.persist(savedCompetition);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateCompetition() {
        assertThat(savedCompetition).isNotNull();
        assertThat(savedCompetition.getId()).isGreaterThan(0);
        assertThat(savedCompetition.getName()).isEqualTo("Competition Name");
    }

    @Test
    @Transactional
    void addIdea() {
        Idea newIdea = new Idea("Another Idea", "Another Description", "Another Features", "Another References", new Date(), "Another Pictures");
        savedCompetition.addIdea(newIdea);
        user.addIdea(newIdea);

        entityManager.persist(newIdea);
        entityManager.flush();

        Competition retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();
        assertThat(retrievedCompetition.getIdeas()).contains(newIdea);
    }

    @Test
    @Transactional
    void removeIdea() {
        Idea newIdea = new Idea("Another Idea", "Another Description", "Another Features", "Another References", new Date(), "Another Pictures");
        savedCompetition.addIdea(newIdea);
        user.addIdea(newIdea);

        entityManager.persist(newIdea);
        entityManager.flush();

        Competition retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();

        Idea ideaToRemove = retrievedCompetition.getIdeas().stream().filter(idea1 -> idea1.getTitle().equals("Another Idea")).findFirst().orElse(null);
        assertThat(ideaToRemove).isNotNull();

        retrievedCompetition.removeIdea(ideaToRemove);
        entityManager.flush();

        retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();
        assertThat(retrievedCompetition.getIdeas()).doesNotContain(ideaToRemove);

        ideaToRemove = ideaRepository.findById(ideaToRemove.getId()).orElse(null);
        assertThat(ideaToRemove).isNull();
    }

    @Test
    @Transactional
    void addIdeaSelection() {
        Competition pulledCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        Idea pulledIdea = ideaRepository.findById(idea.getId()).orElse(null);

        assertThat(pulledCompetition).isNotNull();
        assertThat(pulledIdea).isNotNull();

        IdeaSelection ideaSelection = new IdeaSelection(new Date());

        pulledIdea.addIdeaSelection(ideaSelection);
        pulledCompetition.addIdeaSelection(ideaSelection);

        entityManager.persist(pulledIdea);
        entityManager.flush();
        entityManager.clear();

        Competition retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();
        assertThat(retrievedCompetition.getIdeaSelections()).contains(ideaSelection);
    }

    @Test
    @Transactional
    void removeIdeaSelection() {
        Competition pulledCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        Idea pulledIdea = ideaRepository.findById(idea.getId()).orElse(null);

        assertThat(pulledCompetition).isNotNull();
        assertThat(pulledIdea).isNotNull();

        IdeaSelection ideaSelection = new IdeaSelection(new Date());

        pulledIdea.addIdeaSelection(ideaSelection);
        pulledCompetition.addIdeaSelection(ideaSelection);

        entityManager.persist(pulledCompetition);
        entityManager.flush();
        entityManager.clear();

        Competition retrievedCompetition = competitionRepository.findById(pulledCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();

        IdeaSelection ideaSelectionToRemove = retrievedCompetition.getIdeaSelections().stream().findFirst().orElse(null);
        assertThat(ideaSelectionToRemove).isNotNull();

        retrievedCompetition.removeIdeaSelection(ideaSelectionToRemove);
        entityManager.flush();

        retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();
        assertThat(retrievedCompetition.getIdeaSelections()).doesNotContain(ideaSelectionToRemove);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        entityManager.clear();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
    }
}