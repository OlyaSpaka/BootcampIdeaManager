package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
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
public class VoteServiceTest {

    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private VoteTypeRepository voteTypeRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    private User userToVote;
    private Idea idea;
    private Vote vote;
    private VoteType voteType;
    private Competition competition;
    private User ideaUser;

    //This is confusing even for me... sorry
    @BeforeEach
    // user adds vote to ideaUser's users first idea
    void setUp() {
        ideaUser = userRepository.save(new User("ideaUsername", "email123@example.com", "password123"));
        userToVote = userRepository.save(new User("username", "email@example.com", "password"));
        competition = competitionRepository.save(new Competition("title", "description", new Date(), new Date()));
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferences("Test references");
        ideaUser.addIdea(idea);
        competition.addIdea(idea);
        idea = ideaRepository.save(idea);
        voteType = voteTypeRepository.save(new VoteType("name", 100));


        // Setting up an Idea object for testing


        System.out.println("Competition Id is  " + competition.getId());

        vote = new Vote();
        vote.setVoteType(voteType);
        vote.setIdea(idea);
        vote.setUser(userToVote);


        vote = voteRepository.save(vote);
    }

    //same user adds vote to ideaUser's second idea
    @Test
    void testAddFirstVote() {
        Vote testVote = new Vote();
        Idea newIdea = new Idea();
        newIdea.setCreatedAt(new Date());
        newIdea.setTitle("Test Idea");
        newIdea.setDescription("Test description");
        newIdea.setKeyFeatures("Test features");
        newIdea.setReferences("Test references");
        competition.addIdea(newIdea);
        ideaUser.addIdea(newIdea);
        newIdea = ideaRepository.save(newIdea);


        testVote.setIdea(newIdea);
        testVote.setVoteType(voteType);
        testVote.setUser(userToVote);


        voteService.addVote(testVote);

        List<Vote> votesAfter = voteRepository.findAll();
        assertThat(votesAfter).hasSize(2);


    }

    @Test
        //Test if user can not add second vote on the same idea
    void testAddSecondVote() {
        Vote testVote = new Vote();
        testVote.setIdea(idea);
        testVote.setVoteType(voteType);
        testVote.setUser(userToVote);

        assertThrows(IllegalStateException.class, () -> voteService.addVote(testVote));

    }

    @Test
    void testAddVoteOnYourIdea() {
        Vote testVote = new Vote();
        testVote.setIdea(idea);
        testVote.setVoteType(voteType);
        testVote.setUser(ideaUser);

        assertThrows(IllegalStateException.class, () -> voteService.addVote(testVote));
    }

    @Test
    void testDeleteVoteWhenExist() {
        Vote voteToDelete = new Vote();
        voteToDelete.setIdea(idea);
        voteToDelete.setVoteType(voteType);
        voteToDelete.setUser(ideaUser);

        voteRepository.save(voteToDelete);
        List<Vote> votesBefore = voteRepository.findAll();
        assertThat(votesBefore).contains(voteToDelete);
        voteService.deleteVote(voteToDelete.getId());

        List<Vote> votesAfter = voteRepository.findAll();
        assertThat(votesAfter).doesNotContain(voteToDelete);
    }

    @Test
    void testCheckIdeaVotePoints() {
        VoteType newVoteType = voteTypeRepository.save(new VoteType("very good idea", 55));
        Vote testVote = new Vote();
        testVote.setIdea(idea);
        testVote.setVoteType(newVoteType);
        testVote.setUser(ideaUser);

        voteRepository.save(testVote);
        int expectedPoints = 100 + 55; // Total points from the original vote and the new vote
        int calculatedPoints = voteService.calculatePoints(idea.getId());
        assertThat(calculatedPoints).isEqualTo(expectedPoints);


    }

    @AfterEach
    void cleanUp() {
        voteRepository.deleteAll();
        ideaRepository.deleteAll();
        competitionRepository.deleteAll();
        userRepository.deleteAll();
        voteTypeRepository.deleteAll();

    }
}
