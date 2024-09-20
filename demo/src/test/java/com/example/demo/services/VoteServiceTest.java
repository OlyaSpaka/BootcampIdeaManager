//package com.example.demo.services;
//
//import com.example.demo.models.*;
//import com.example.demo.repositories.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.Instant;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class VoteServiceTest {
//    @InjectMocks
//    private VoteService voteService;
//    @Mock
//    private VoteRepository voteRepository;
//    @Mock
//    private IdeaRepository ideaRepository;
//    @Mock
//    private IdeaSelectionService ideaSelectionService;
//    @Mock
//    private CompetitionRepository competitionRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testAddFirstVote() {
//        // Arrange
//        User user = new User();
//        user.setId(1);
//        Idea idea = new Idea();
//        idea.setId(2);
//        idea.setUser(user); // Owner of the idea
//
//        Vote vote = new Vote();
//        vote.setUser(new User());
//        vote.setIdea(idea);
//
//        when(voteRepository.findByUserIdAndIdeaId(anyInt(), anyInt())).thenReturn(Optional.empty());
//
//        // Act
//        voteService.addVote(vote);
//
//        // Assert
//        verify(voteRepository, times(1)).save(vote);
//    }
//
//    @Test
//    void testAddVote_UserVotesOwnIdea() {
//        // Arrange
//        User user = new User();
//        user.setId(1);
//        Idea idea = new Idea();
//        idea.setId(1); // Same ID as user
//        idea.setUser(user);
//
//        Vote vote = new Vote();
//        vote.setUser(user);
//        vote.setIdea(idea);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class, () -> voteService.addVote(vote));
//        verify(voteRepository, never()).save(vote);
//    }
//
//    @Test
//    void testAddVote_UserAlreadyVoted() {
//        // Arrange
//        User user = new User();
//        user.setId(1);
//        Idea idea = new Idea();
//        idea.setId(2);
//        idea.setUser(new User());
//
//        Vote vote = new Vote();
//        vote.setUser(user);
//        vote.setIdea(idea);
//
//        when(voteRepository.findByUserIdAndIdeaId(anyInt(), anyInt())).thenReturn(Optional.of(vote));
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class, () -> voteService.addVote(vote));
//        verify(voteRepository, never()).save(vote);
//    }
//
//    @Test
//    void testDeleteVote() {
//        // Arrange
//        Integer voteId = 1;
//        when(voteRepository.existsById(voteId)).thenReturn(true);
//
//        // Act
//        voteService.deleteVote(voteId);
//
//        // Assert
//        verify(voteRepository, times(1)).deleteById(voteId);
//    }
//
//    @Test
//    void testDeleteVote_NotExists() {
//        // Arrange
//        Integer voteId = 1;
//        when(voteRepository.existsById(voteId)).thenReturn(false);
//
//        // Act & Assert
//        assertThrows(IllegalStateException.class, () -> voteService.deleteVote(voteId));
//        verify(voteRepository, never()).deleteById(voteId);
//    }
//    @Test
//    void testCalculatePoints() {
//        // Arrange
//        Integer ideaId = 1;
//        Vote vote1 = new Vote();
//        VoteType voteType1 = new VoteType();
//        voteType1.setPoints(10);
//        vote1.setVoteType(voteType1);
//
//        Vote vote2 = new Vote();
//        VoteType voteType2 = new VoteType();
//        voteType2.setPoints(20);
//        vote2.setVoteType(voteType2);
//
//        List<Vote> votes = Arrays.asList(vote1, vote2);
//        when(voteRepository.findByIdeaId(ideaId)).thenReturn(votes);
//
//        // Act
//        int totalPoints = voteService.calculatePoints(ideaId);
//
//        // Assert
//        assertEquals(30, totalPoints);
//    }
//
//    @Test
//    void testGetAllPoints() {
//        // Arrange
//        Idea idea1 = new Idea();
//        idea1.setId(1);
//        Idea idea2 = new Idea();
//        idea2.setId(2);
//
//        VoteType voteType1 = new VoteType();
//        voteType1.setPoints(10);
//
//        Vote vote1_1 = new Vote();
//        vote1_1.setVoteType(voteType1);
//        Vote vote2_1 = new Vote();
//        vote2_1.setVoteType(voteType1);
//        Vote vote2_2 = new Vote();
//        vote2_2.setVoteType(voteType1);
//
//        List<Vote> votes1 = List.of(vote1_1);
//        List<Vote> votes2 = List.of(vote2_1,vote2_2);
//
//        List<Idea> ideas = Arrays.asList(idea1, idea2);
//
//        when(ideaRepository.findAll()).thenReturn(ideas);
//        when(voteRepository.findByIdeaId(1)).thenReturn(votes1);
//        when(voteRepository.findByIdeaId(2)).thenReturn(votes2);
//
//        // Act
//        HashMap<Integer, Integer> pointsMap = voteService.getAllPoints(1);
//
//        // Assert
//        assertEquals(10, pointsMap.get(1));
//        assertEquals(20, pointsMap.get(2));
//    }
//
//    @Test
//    void testGetWinningIdeas() {
//        // Arrange
//        Integer competitionId = 1;
//        Competition competition = new Competition();
//        competition.setAmountOfWinners(1);
//
//        VoteType voteType1 = new VoteType();
//        voteType1.setPoints(10);
//
//        Vote vote1 = new Vote();
//        vote1.setVoteType(voteType1);
//
//        Idea idea1 = new Idea();
//        idea1.setId(1);
//        idea1.addVote(vote1);
//
//        Vote vote2 = new Vote();
//        vote2.setVoteType(voteType1);
//
//        Idea idea2 = new Idea();
//        idea2.setId(2);
//        idea2.addVote(vote2);
//
//        List<Idea> ideas = Arrays.asList(idea1, idea2);
//
//        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));
//        when(ideaRepository.findAll()).thenReturn(ideas);
//        when(voteRepository.findByIdeaId(1)).thenReturn(List.of(vote1));
//        when(voteRepository.findByIdeaId(2)).thenReturn(List.of(vote2));
//        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea1));
//        when(ideaRepository.findById(2)).thenReturn(Optional.of(idea2));
//
//        // Act
//        List<Idea> winningIdeas = voteService.getWinningIdeas(competitionId);
//
//        // Assert
//        assertEquals(1, winningIdeas.size());
//        assertEquals(1, winningIdeas.get(0).getId());
//    }
//    @Test
//    void testAddWinningIdeasToIdeaSelectionRepository() {
//        // Arrange
//        Integer competitionId = 1;
//        Idea idea1 = new Idea();
//        idea1.setId(1);
//        idea1.setCreatedAt(Date.from(Instant.now()));
//
//        Competition competition = new Competition();
//        competition.setId(competitionId);
//        competition.setAmountOfWinners(1); // Set the number of winners expected
//
//        List<Idea> winningIdeas = List.of(idea1);
//
//        // Mocking
//        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));
//        when(voteService.getWinningIdeas(competitionId)).thenReturn(winningIdeas);
//        when(competitionRepository.findById(competitionId)).thenReturn(Optional.of(competition));
//        when(ideaRepository.findById(1)).thenReturn(Optional.of(idea1));
//
//        // Act
//        voteService.addWinningIdeasToIdeaSelectionRepository(competitionId);
//
//        // Assert
//        verify(ideaSelectionService, times(1)).addIdeaSelection(any(IdeaSelection.class));
//        ArgumentCaptor<IdeaSelection> ideaSelectionCaptor = ArgumentCaptor.forClass(IdeaSelection.class);
//        verify(ideaSelectionService).addIdeaSelection(ideaSelectionCaptor.capture());
//
//        IdeaSelection capturedIdeaSelection = ideaSelectionCaptor.getValue();
//        assertEquals(idea1, capturedIdeaSelection.getIdea());
//        assertEquals(competition, capturedIdeaSelection.getCompetition());
//    }
//
//}
