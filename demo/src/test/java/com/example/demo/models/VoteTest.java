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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class VoteTest {
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoteTypeRepository voteTypeRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private User user;
    private Idea savedIdea;
    private VoteType voteType;
    private Vote vote;

    @BeforeEach
    @Transactional
    void setUp() {
        Competition competition = competitionRepository.save(new Competition("Competition Name", "Description", new Date(), new Date(), 3));
        user = userRepository.save(new User("unittestuser", "email@example.com", "password"));
        Idea idea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        user.addIdea(idea);
        competition.addIdea(idea);

        savedIdea = ideaRepository.save(idea);
        entityManager.flush();

        voteType = voteTypeRepository.save(new VoteType("Type1", 10));
        vote = new Vote();
        voteType.addVote(vote);
        user.addVote(vote);
        savedIdea.addVote(vote);

        entityManager.persist(vote);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    public void testVoteConstructor() {
        assertNotNull(vote.getId());
        assertEquals(voteType, vote.getVoteType());
        assertEquals(user, vote.getUser());
        assertEquals(savedIdea, vote.getIdea());
    }

    @Test
    @Transactional
    public void testAddVote() {
        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNotNull();
        assertEquals(retrievedVote.getVoteType(), voteType);
        assertEquals(retrievedVote.getUser(), user);
        assertEquals(retrievedVote.getIdea(), savedIdea);
    }

    @Test
    @Transactional
    public void testRemoveVote() {
        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNotNull();

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();

        // Remove the vote
        retrievedUser.removeVote(retrievedVote);
        entityManager.flush();

        Vote deletedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(deletedVote).isNull();

        // Assert vote is removed from User and Idea
        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getVotes()).doesNotContain(retrievedVote);

        Idea updatedIdea = ideaRepository.findById(savedIdea.getId()).orElse(null);
        assertThat(updatedIdea).isNotNull();
        assertThat(updatedIdea.getVotes()).doesNotContain(retrievedVote);

        // Assert vote is deleted from the database

    }

    @Test
    @Transactional
    public void testVoteTypeAssociation() {
        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNotNull();
        assertThat(retrievedVote.getVoteType()).isEqualTo(voteType);
    }

    @Test
    @Transactional
    public void testUserAssociation() {
        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNotNull();
        assertThat(retrievedVote.getUser()).isEqualTo(user);
    }

    @Test
    @Transactional
    public void testIdeaAssociation() {
        Vote retrievedVote = voteRepository.findById(vote.getId()).orElse(null);
        assertThat(retrievedVote).isNotNull();
        assertThat(retrievedVote.getIdea()).isEqualTo(savedIdea);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        voteRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
        voteTypeRepository.deleteAll();
    }
}