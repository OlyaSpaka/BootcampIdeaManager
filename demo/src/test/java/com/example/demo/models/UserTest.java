package com.example.demo.models;

import com.example.demo.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class UserTest {
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private User user;
    private Idea savedIdea;
    @BeforeEach
    @Transactional
    public void setUp() {
        LocalDate startDate = LocalDate.of(2024,9,01);
        LocalDate endDate = LocalDate.of(2024,10,01);

        Competition competition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        user = userRepository.save(new User("unittestuser", "email@example.com", "password"));
        Idea idea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        user.addIdea(idea);
        competition.addIdea(idea);

        savedIdea = ideaRepository.save(idea);
        entityManager.flush();
    }

    @Test
    @Transactional
    public void testUserConstructor() {
        assertEquals("unittestuser", user.getUsername());
        assertEquals("email@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }

    @Test
    @Transactional
    public void testAddComment() {
        Comment comment = new Comment("This is a comment");
        user.addComment(comment);
        savedIdea.addComment(comment);

        entityManager.persist(user);
        entityManager.flush();

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getComments()).isNotNull();

        Comment retrievedComment = retrievedUser.getComments().stream()
                .findFirst()
                .orElse(null);
        assertThat(retrievedComment).isNotNull();

        assertEquals(retrievedComment, comment);
    }

    @Test
    @Transactional
    public void testAddBookmark() {
        Bookmark bookmark = new Bookmark();
        user.addBookmark(bookmark);
        savedIdea.addBookmark(bookmark);

        entityManager.persist(user);
        entityManager.flush();

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getBookmarks()).isNotNull();

        Bookmark retrievedBookmark = retrievedUser.getBookmarks().stream()
                .findFirst()
                .orElse(null);
        assertThat(retrievedBookmark).isNotNull();

        assertEquals(retrievedBookmark, bookmark);
    }

    @Test
    @Transactional
    public void testAddVote() {
        VoteType voteType = new VoteType("Type1", 10);
        entityManager.persist(voteType);

        Vote vote = new Vote();
        voteType.addVote(vote);
        user.addVote(vote);
        savedIdea.addVote(vote);

        entityManager.persist(savedIdea); // Save Idea and associated Vote
        entityManager.flush(); // Ensure changes are committed
        entityManager.clear();

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getBookmarks()).isNotNull();

        Vote newVote = retrievedUser.getVotes().stream().findFirst().orElse(null);
        assertThat(newVote).isNotNull(); // Ensure Vote is present before removal

        retrievedUser.removeVote(newVote);
        entityManager.flush();

        retrievedUser = userRepository.findById(retrievedUser.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull(); // Assert that the Vote is removed
        assertThat(retrievedUser.getVotes()).doesNotContain(newVote);

        newVote = voteRepository.findById(newVote.getId()).orElse(null);
        assertThat(newVote).isNull();
    }

    @Test
    @Transactional
    public void testAddIdea() {
        User newUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(newUser).isNotNull();
        assertThat(newUser.getIdeas()).isNotNull();

        Idea pulledIdea = newUser.getIdeas().stream().findFirst().orElse(null);
        assertEquals(savedIdea, pulledIdea);
    }

    @Test
    @Transactional
    public void testAddRole() {
        Role role = new Role("USER");
        user.addRole(role);

        entityManager.persist(role);

//        entityManager.persist(username); // Save Idea and associated Vote
        entityManager.flush(); // Ensure changes are committed

        User pulledUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(pulledUser).isNotNull();

        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
    }

    @Test
    @Transactional
    public void testRemoveRole() {
        Role role = new Role("ADMIN");
        user.addRole(role);

        entityManager.persist(role);
        entityManager.flush();

        User pulledUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(pulledUser).isNotNull();

        pulledUser.removeRole(role);

        entityManager.persist(user);
        entityManager.flush();

        pulledUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(pulledUser).isNotNull();

        assertFalse(user.getRoles().contains(role));
        assertFalse(role.getUsers().contains(user));
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test if necessary
        voteRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
    }
}