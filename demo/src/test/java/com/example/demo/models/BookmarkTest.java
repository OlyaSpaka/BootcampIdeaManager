package com.example.demo.models;

import com.example.demo.repositories.BookmarkRepository;
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
class BookmarkTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private Idea idea;
    private Competition competition;
    private Bookmark savedBookmark;

    @BeforeEach
    @Transactional
    void setUp() {
        // Setup initial data
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,9,1);
        Idea newIdea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");
        competition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        user = userRepository.save(new User("username", "email@example.com", "password"));

        user.addIdea(newIdea);
        competition.addIdea(newIdea);

        idea = ideaRepository.save(newIdea);
        entityManager.flush();

        // Create and persist a Bookmark
        Bookmark bookmark = new Bookmark();
        user.addBookmark(bookmark);
        idea.addBookmark(bookmark);

        savedBookmark = bookmarkRepository.save(bookmark);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateBookmark() {
        assertThat(savedBookmark).isNotNull();
        assertThat(savedBookmark.getId()).isGreaterThan(0);
        assertThat(savedBookmark.getUser()).isEqualTo(user);
        assertThat(savedBookmark.getIdea()).isEqualTo(idea);
    }

    @Test
    @Transactional
    void addBookmark() {
        // Create and persist new entities
        User newUser = new User("anotherUser", "another@example.com", "password");
        Idea newIdea = new Idea("New Idea", "Description", "Key Features", "References", new Date(), "Pictures");

        newUser.addIdea(newIdea);
        competition.addIdea(newIdea);

        entityManager.persist(newUser);
        entityManager.flush();
        entityManager.clear();
//        ideaRepository.save(newIdea);

        Bookmark bookmark = new Bookmark();
        newIdea.addBookmark(bookmark);
        newUser.addBookmark(bookmark);

        bookmarkRepository.save(bookmark);
        entityManager.flush();
        entityManager.clear();

        // Verify that the new bookmark is associated with the new username and idea
        Bookmark retrievedBookmark = bookmarkRepository.findById(bookmark.getId()).orElse(null);
        assertThat(retrievedBookmark).isNotNull();
        assertThat(retrievedBookmark.getUser()).isEqualTo(newUser);
        assertThat(retrievedBookmark.getIdea()).isEqualTo(newIdea);
    }

    @Test
    @Transactional
    void removeBookmark() {
        Bookmark bookmarkToRemove = new Bookmark();
        user.addBookmark(bookmarkToRemove);
        idea.addBookmark(bookmarkToRemove);

        entityManager.persist(bookmarkToRemove);
        entityManager.flush();
        entityManager.clear();

        Bookmark retrievedBookmark = bookmarkRepository.findById(bookmarkToRemove.getId()).orElse(null);
        assertThat(retrievedBookmark).isNotNull();

        bookmarkRepository.delete(retrievedBookmark);
        entityManager.flush();

        // Verify that the bookmark is removed
        retrievedBookmark = bookmarkRepository.findById(bookmarkToRemove.getId()).orElse(null);
        assertThat(retrievedBookmark).isNull();
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test
        entityManager.clear();
        bookmarkRepository.deleteAll();
        ideaRepository.deleteAll();
        userRepository.deleteAll();
    }
}
