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
    @Autowired
    private IdeaSelectionRepository ideaSelectionRepository;
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
        competition = competitionRepository.save(new Competition("title", "description", new Date(), new Date(), 2));
        idea = new Idea();
        idea.setCreatedAt(new Date());
        idea.setTitle("Test Idea");
        idea.setDescription("Test description");
        idea.setKeyFeatures("Test features");
        idea.setReferenceLinks("Test references");
        ideaUser.addIdea(idea);
        competition.addIdea(idea);
        idea = ideaRepository.save(idea);
        voteType = voteTypeRepository.save(new VoteType("name", 200));


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
        newIdea.setReferenceLinks("Test references");
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


    @Test
    void testGetCompetitionWinningIdeasWhenExists() {
        // Create more vote types with different points
        VoteType lowVoteType = voteTypeRepository.save(new VoteType("low points", 50));
        VoteType highVoteType = voteTypeRepository.save(new VoteType("high points", 150));

        // Create more ideas and assign votes with varying points
        Idea secondIdea = new Idea();
        secondIdea.setCreatedAt(new Date());
        secondIdea.setTitle("Second Test Idea");
        secondIdea.setDescription("Test description");
        secondIdea.setKeyFeatures("Test features");
        secondIdea.setReferenceLinks("Test references");
        ideaUser.addIdea(secondIdea);
        competition.addIdea(secondIdea);
        secondIdea = ideaRepository.save(secondIdea);

        Idea thirdIdea = new Idea();
        thirdIdea.setCreatedAt(new Date());
        thirdIdea.setTitle("Third Test Idea");
        thirdIdea.setDescription("Test description");
        thirdIdea.setKeyFeatures("Test features");
        thirdIdea.setReferenceLinks("Test references");
        ideaUser.addIdea(thirdIdea);
        competition.addIdea(thirdIdea);
        thirdIdea = ideaRepository.save(thirdIdea);

        // Add votes with different point values for each idea
        Vote vote1 = new Vote();
        vote1.setIdea(idea);  // First idea from setUp()
        vote1.setVoteType(lowVoteType);
        vote1.setUser(userToVote);
        voteRepository.save(vote1);

        Vote vote2 = new Vote();
        vote2.setIdea(secondIdea);  // Second idea
        vote2.setVoteType(highVoteType);
        vote2.setUser(userToVote);
        voteRepository.save(vote2);

        Vote vote3 = new Vote();
        vote3.setIdea(thirdIdea);  // Third idea
        vote3.setVoteType(voteType);  // Default voteType with 100 points
        vote3.setUser(userToVote);
        voteRepository.save(vote3);

        // Test getWinningIdeas() for the top 2 ideas by vote points
        List<Idea> winningIdeas = voteService.getWinningIdeas(competition.getId());
        for(Idea idea :winningIdeas){
            System.out.println("Idea id:" + idea.getId() + "Idea points:" + voteService.calculatePoints(idea.getId()));
        }
        // Verify that the top 2 ideas are returned in descending order of points
        assertThat(winningIdeas).hasSize(2);
        assertThat(winningIdeas.get(0).getId()).isEqualTo(idea.getId()); // Second idea should be first (150 points)
        assertThat(winningIdeas.get(1).getId()).isEqualTo(thirdIdea.getId()); // Third idea should be second (100 points)
    }

    @Test
    void testAddWinningIdeasToIdeaSelectionRepository() {
        // Given: Setup necessary data
        VoteType highVoteType = voteTypeRepository.save(new VoteType("High Points", 300));
        VoteType lowVoteType = voteTypeRepository.save(new VoteType("Low Points", 50));

        // Create more ideas
        Idea idea1 = new Idea();
        idea1.setCreatedAt(new Date());
        idea1.setTitle("High Points Idea");
        idea1.setDescription("Idea with high points");
        idea1.setKeyFeatures("Key Features");
        idea1.setReferenceLinks("Reference Links");
        ideaUser.addIdea(idea1);
        competition.addIdea(idea1);
        idea1 = ideaRepository.save(idea1);

        Idea idea2 = new Idea();
        idea2.setCreatedAt(new Date());
        idea2.setTitle("Low Points Idea");
        idea2.setDescription("Idea with low points");
        idea2.setKeyFeatures("Key Features");
        idea2.setReferenceLinks("Reference Links");
        ideaUser.addIdea(idea2);
        competition.addIdea(idea2);
        idea2 = ideaRepository.save(idea2);

        // Cast votes
        Vote vote1 = new Vote();
        vote1.setIdea(idea1);
        vote1.setVoteType(highVoteType);
        vote1.setUser(userToVote);
        voteRepository.save(vote1);

        Vote vote2 = new Vote();
        vote2.setIdea(idea2);
        vote2.setVoteType(lowVoteType);
        vote2.setUser(userToVote);
        voteRepository.save(vote2);

        // Call the method under test
        voteService.addWinningIdeasToIdeaSelectionRepository(competition.getId());

        // Verify that IdeaSelectionService.addIdeaSelection() was called
        // with the correct IdeaSelection objects
        List<IdeaSelection> ideaSelections = ideaSelectionRepository.findAll();
        assertThat(ideaSelections).hasSize(2);

        // Verify that IdeaSelection objects have been created with the expected values
        for (IdeaSelection ideaSelection : ideaSelections) {
            assertThat(ideaSelection.getCompetition()).isEqualTo(competition);
            assertThat(competition.getIdeas()).contains(ideaSelection.getIdea());
        }


        IdeaSelection firstSelection = ideaSelections.get(0);
        IdeaSelection secondSelection = ideaSelections.get(1);

        assertThat(firstSelection.getIdea().getId()).isEqualTo(idea1.getId());
        assertThat(secondSelection.getIdea().getId()).isEqualTo(idea.getId());
        assertThat(firstSelection.getIdea().getId()).isNotEqualTo(secondSelection.getIdea().getId());
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
