package com.example.demo.models;

import com.example.demo.repositories.CategoryRepository;
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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private Category savedCategory;
    private Idea savedIdea;
    private User savedUser;
    private Competition savedCompetition;

    @BeforeEach
    @Transactional
    void setUp() {
        // Setup initial data
        savedCategory = categoryRepository.save(new Category("Category Name"));
        savedCompetition = competitionRepository.save(new Competition("Competition Name", "Description", new Date(), new Date(), 3));
        savedUser = userRepository.save(new User("username", "email@example.com", "password"));

        savedIdea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        savedUser.addIdea(savedIdea);
        savedCompetition.addIdea(savedIdea);
//        savedCategory.addIdea(savedIdea);

        entityManager.persist(savedIdea);

        entityManager.flush();
    }

    @Test
    @Transactional
    void testCreateCategory() {
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isGreaterThan(0);
        assertThat(savedCategory.getName()).isEqualTo("Category Name");
    }

    @Test
    @Transactional
    void addIdeaToCategory() {
        Idea newIdea = new Idea("New Idea", "New Description", "Key Features", "References", new Date(), "Pictures");

        savedCompetition.addIdea(newIdea);
        savedUser.addIdea(newIdea);

        entityManager.persist(newIdea);
        entityManager.flush();

        savedCategory.addIdea(newIdea);

        entityManager.persist(savedCategory);
        entityManager.flush();

        Category retrievedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory.getIdeas()).contains(newIdea);

        Idea retrievedIdea = ideaRepository.findById(newIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getCategories()).contains(savedCategory);
    }

    @Test
    @Transactional
    void removeIdeaFromCategory() {
        // Add idea to the category first
        savedCategory.addIdea(savedIdea);

        entityManager.persist(savedCategory);
        entityManager.flush();

        // Remove the idea from the category
        savedCategory.removeIdea(savedIdea);

        entityManager.persist(savedCategory);
        entityManager.flush();

        Category retrievedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory.getIdeas()).doesNotContain(savedIdea);

        Idea retrievedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(retrievedIdea).isNotNull();
        assertThat(retrievedIdea.getCategories()).doesNotContain(savedCategory);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test
        entityManager.clear();
        categoryRepository.deleteAll();
        ideaRepository.deleteAll();
    }
}
