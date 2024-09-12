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

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VoteTypeTest {

    @Autowired
    private VoteTypeRepository voteTypeRepository;
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
    private VoteType voteType;
    private Idea idea;
    private User user;

    @BeforeEach
    void setUp() {
        Competition competition = competitionRepository.save(new Competition("Competition Name", "Description", new Date(), new Date()));
        user = userRepository.save(new User("username", "email@example.com", "password"));
        idea = new Idea("Idea Title", "Idea Description", "Key Features", "References", new Date(), "Pictures");

        competition.addIdea(idea);
        user.addIdea(idea);

        entityManager.persist(idea);
        entityManager.flush();

        voteType = new VoteType("Upvote", 10);
        voteTypeRepository.save(voteType);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testSaveVoteType() {
        VoteType savedVoteType = voteTypeRepository.save(voteType);

        assertThat(savedVoteType).isNotNull();
        assertThat(savedVoteType.getId()).isNotNull();
        assertThat(savedVoteType.getName()).isEqualTo("Upvote");
        assertThat(savedVoteType.getPoints()).isEqualTo(10);
    }

    @Test
    void testAddVoteToVoteType() {
        Vote vote = new Vote();
        idea.addVote(vote);
        user.addVote(vote);
        voteType.addVote(vote);

        entityManager.persist(vote);
        entityManager.flush();
        entityManager.clear();

        VoteType savedVoteType = voteTypeRepository.findById(voteType.getId()).orElseThrow();
        assertThat(savedVoteType.getVotes()).hasSize(1);

        List<Vote> votes = voteRepository.findAll();
        assertThat(votes).hasSize(1);
        assertThat(votes.get(0).getVoteType()).isEqualTo(savedVoteType);
    }

    @Test
    void testRemoveVoteFromVoteType() {
        Vote vote = new Vote();
        idea.addVote(vote);
        user.addVote(vote);
        voteType.addVote(vote);

        entityManager.persist(vote);
        entityManager.flush();
        entityManager.clear();

        voteType.removeVote(vote);
        voteRepository.delete(vote);

        entityManager.flush();
        entityManager.clear();

        VoteType savedVoteType = voteTypeRepository.findById(voteType.getId()).orElseThrow();
        assertThat(savedVoteType.getVotes()).isEmpty();
    }

    @Test
    void testCascadeDeleteVotes() {
        VoteType newVoteType = new VoteType("Upvote", 10);

        Vote vote1 = new Vote();
        idea.addVote(vote1);
        user.addVote(vote1);
        newVoteType.addVote(vote1);
        Vote vote2 = new Vote();
        idea.addVote(vote2);
        user.addVote(vote2);
        newVoteType.addVote(vote2);

        entityManager.persist(newVoteType);
        entityManager.flush();

        voteTypeRepository.delete(newVoteType);
        entityManager.flush();

        List<Vote> remainingVotes = voteRepository.findAll();
        assertThat(remainingVotes).isEmpty();
    }

    @Test
    void testVoteTypeEquality() {
        VoteType voteType2 = new VoteType("Downvote", -5);
        voteTypeRepository.save(voteType2);

        assertThat(voteType).isNotEqualTo(voteType2);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        entityManager.clear();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
    }
}
