package com.example.demo.models;

import com.example.demo.repositories.CommentRepository;
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
class CommentTest {

    @Autowired
    private CommentRepository commentRepository;
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
    private Comment savedComment;

    @BeforeEach
    @Transactional
    void setUp() {
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,9,1);

        // Setup initial data
        user = userRepository.save(new User("username", "email@example.com", "password"));
        Competition competition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        Idea newIdea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        user.addIdea(newIdea);
        competition.addIdea(newIdea);

        idea = ideaRepository.save(newIdea);
        entityManager.flush();

        // Create and persist a Comment
        Comment comment = new Comment("This is a comment");
        user.addComment(comment);
        idea.addComment(comment);

        savedComment = commentRepository.save(comment);
        entityManager.flush();
    }

    @Test
    void testCreateComment() {
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isGreaterThan(0);
        assertThat(savedComment.getContent()).isEqualTo("This is a comment");
        assertThat(savedComment.getUser()).isEqualTo(user);
        assertThat(savedComment.getIdea()).isEqualTo(idea);
    }

    @Test
    @Transactional
    void addComment() {
        // Create and persist new entities
        LocalDate startDate = LocalDate.of(2024,9,1);
        LocalDate endDate = LocalDate.of(2024,9,1);
        Competition competition = competitionRepository.save(new Competition("Competition Name", "Description", startDate, endDate, 3));
        User newUser = new User("anotherUser", "another@example.com", "password");
        Idea newIdea = new Idea("New Idea", "Description", "Key Features", "References", new Date(), "Pictures");

        competition.addIdea(newIdea);
        newUser.addIdea(newIdea);

        entityManager.persist(newUser);
        entityManager.flush();

//        userRepository.save(newUser);
//        ideaRepository.save(newIdea);

        Comment comment = new Comment("New Comment");
        newUser.addComment(comment);
        newIdea.addComment(comment);

        entityManager.persist(newUser);
        entityManager.flush();

        // Verify that the new comment is associated with the new username and idea
        Comment retrievedComment = commentRepository.findById(comment.getId()).orElse(null);
        assertThat(retrievedComment).isNotNull();
        assertThat(retrievedComment.getUser()).isEqualTo(newUser);
        assertThat(retrievedComment.getIdea()).isEqualTo(newIdea);
    }

    @Test
    @Transactional
    void removeComment() {
        Comment commentToRemove = new Comment("Comment to Remove");
        user.addComment(commentToRemove);
        idea.addComment(commentToRemove);

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        Comment retrievedComment = commentRepository.findById(commentToRemove.getId()).orElse(null);
        assertThat(retrievedComment).isNotNull();

        commentRepository.delete(retrievedComment);
        entityManager.flush();

        // Verify that the comment is removed
        retrievedComment = commentRepository.findById(commentToRemove.getId()).orElse(null);
        assertThat(retrievedComment).isNull();
    }

    @AfterEach
    @Transactional
    void tearDown() {
        // Clean up after each test
        commentRepository.deleteAll();
        ideaRepository.deleteAll();
        userRepository.deleteAll();
    }
}
