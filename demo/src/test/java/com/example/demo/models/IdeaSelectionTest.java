package com.example.demo.models;

import com.example.demo.repositories.IdeaRepository;
import com.example.demo.repositories.IdeaSelectionRepository;
import com.example.demo.repositories.CompetitionRepository;
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
class IdeaSelectionTest {

    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private IdeaSelectionRepository ideaSelectionRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Idea savedIdea;
    private Competition savedCompetition;
    private IdeaSelection savedIdeaSelection;

    @BeforeEach
    @Transactional
    void setUp() {
        // Setup initial data
        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 10);

        savedCompetition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        User savedUser = userRepository.save(new User("username", "email@example.com", "password"));

        savedIdea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");
        savedCompetition.addIdea(savedIdea);
        savedUser.addIdea(savedIdea);

        savedIdea = ideaRepository.save(savedIdea);

        savedIdeaSelection = new IdeaSelection(new Date());
        savedIdea.addIdeaSelection(savedIdeaSelection);
        savedCompetition.addIdeaSelection(savedIdeaSelection);

        savedIdeaSelection = ideaSelectionRepository.save(savedIdeaSelection);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    void testCreateIdeaSelection() {
        assertThat(savedIdeaSelection).isNotNull();
        assertThat(savedIdeaSelection.getId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    void addIdeaSelectionToIdeaAndCompetition() {
        IdeaSelection newIdeaSelection = new IdeaSelection(new Date());
        savedIdea.addIdeaSelection(newIdeaSelection);
        savedCompetition.addIdeaSelection(newIdeaSelection);

        entityManager.persist(newIdeaSelection);
        entityManager.flush();

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getIdeaSelections()).contains(newIdeaSelection);

        Competition retrievedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(retrievedCompetition).isNotNull();
        assertThat(retrievedCompetition.getIdeaSelections()).contains(newIdeaSelection);
    }

    @Test
    @Transactional
    void removeIdeaSelectionFromIdeaAndCompetition() {
        // Remove the IdeaSelection from the Idea and Competition
        IdeaSelection existingIdeaSelection = ideaSelectionRepository.findById(savedIdeaSelection.getId()).orElse(null);
        assertThat(existingIdeaSelection).isNotNull();

        savedIdea.getIdeaSelections().remove(existingIdeaSelection);
        savedCompetition.getIdeaSelections().remove(existingIdeaSelection);

        entityManager.remove(existingIdeaSelection);
        entityManager.flush();

        Idea updatedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(updatedIdea).isNotNull();
        assertThat(updatedIdea.getIdeaSelections()).doesNotContain(existingIdeaSelection);

        Competition updatedCompetition = competitionRepository.findById(savedCompetition.getId()).orElse(null);
        assertThat(updatedCompetition).isNotNull();
        assertThat(updatedCompetition.getIdeaSelections()).doesNotContain(existingIdeaSelection);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test
        entityManager.clear();
        ideaSelectionRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
    }
}
