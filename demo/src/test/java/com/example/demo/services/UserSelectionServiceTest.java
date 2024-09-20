package com.example.demo.services;

import com.example.demo.dto.GroupAssignmentsDTO;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSelectionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private UserSelectionPrioritiesRepository userSelectionPrioritiesRepository;

    @Mock
    private UserSelectionResultRepository userSelectionResultRepository;

    @Mock
    private IdeaSelectionRepository ideaSelectionRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserSelectionService userSelectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserSelections() {
        // Arrange
        Integer userId = 116;
        Map<Integer, Integer> priorities = Map.of(6, 1, 3, 2);
        Timestamp submissionTimestamp = Timestamp.valueOf("2024-09-19 22:34:49");

        User user = new User();
        user.setId(userId);

        Idea idea1 = new Idea();
        idea1.setId(6);
        IdeaSelection ideaSelection1 = new IdeaSelection();
        ideaSelection1.setIdea(idea1);

        Idea idea2 = new Idea();
        idea2.setId(3);
        IdeaSelection ideaSelection2 = new IdeaSelection();
        ideaSelection2.setIdea(idea2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ideaRepository.findIdeaSelections(priorities.keySet()))
                .thenReturn(List.of(ideaSelection1, ideaSelection2));

        when(userRepository.countUsers()).thenReturn(10);
        when(ideaSelectionRepository.findAll()).thenReturn(List.of(ideaSelection1, ideaSelection2));

        // Act
        userSelectionService.saveUserSelections(userId, priorities, submissionTimestamp);

        // Assert
        verify(userSelectionPrioritiesRepository, times(2)).save(any(UserSelectionPriorities.class));
        verify(userRepository, times(1)).findById(userId);
        verify(ideaRepository, times(1)).findIdeaSelections(priorities.keySet());

        ArgumentCaptor<UserSelectionPriorities> captor = ArgumentCaptor.forClass(UserSelectionPriorities.class);
        verify(userSelectionPrioritiesRepository, times(2)).save(captor.capture());

        List<UserSelectionPriorities> savedSelections = captor.getAllValues();

        assertAll(
                () -> assertTrue(savedSelections.stream().anyMatch(s ->
                        s.getIdeaSelection().getIdea().getId() == 6 &&
                                s.getPriority() == 1 &&
                                s.getSubmissionTime().equals(submissionTimestamp)
                ), "Expected selection with Idea ID 6 and Priority 1"),

                () -> assertTrue(savedSelections.stream().anyMatch(s ->
                        s.getIdeaSelection().getIdea().getId() == 3 &&
                                s.getPriority() == 2 &&
                                s.getSubmissionTime().equals(submissionTimestamp)
                ), "Expected selection with Idea ID 3 and Priority 2")
        );
    }



    @Test
    void assignUsersToGroups() {
        // Arrange
        Idea idea1 = new Idea();
        idea1.setId(6);

        Idea idea2 = new Idea();
        idea2.setId(3);

        User user1 = new User();
        user1.setId(65);

        User user2 = new User();
        user2.setId(100);

        IdeaSelection ideaSelection1 = new IdeaSelection();
        ideaSelection1.setIdea(idea1);
        ideaSelection1.setId(6);

        IdeaSelection ideaSelection2 = new IdeaSelection();
        ideaSelection2.setIdea(idea2);
        ideaSelection2.setId(3);

        UserSelectionPriorities priority1 = new UserSelectionPriorities();
        priority1.setId(115);
        priority1.setUser(user1);
        priority1.setIdeaSelection(ideaSelection1);
        priority1.setPriority(1);
        priority1.setSubmissionTime(Timestamp.valueOf("2024-09-19 13:46:02"));

        UserSelectionPriorities priority2 = new UserSelectionPriorities();
        priority2.setId(116);
        priority2.setUser(user1);
        priority2.setIdeaSelection(ideaSelection2);
        priority2.setPriority(2);
        priority2.setSubmissionTime(Timestamp.valueOf("2024-09-19 13:46:02"));

        UserSelectionPriorities priority3 = new UserSelectionPriorities();
        priority3.setId(117);
        priority3.setUser(user2);
        priority3.setIdeaSelection(ideaSelection1);
        priority3.setPriority(1);
        priority3.setSubmissionTime(Timestamp.valueOf("2024-09-19 13:45:02"));

        UserSelectionPriorities priority4 = new UserSelectionPriorities();
        priority4.setId(118);
        priority4.setUser(user2);
        priority4.setIdeaSelection(ideaSelection2);
        priority4.setPriority(2);
        priority4.setSubmissionTime(Timestamp.valueOf("2024-09-19 13:45:02"));

        List<UserSelectionPriorities> allPriorities = new ArrayList<>(List.of(priority1, priority2, priority3, priority4));
        List<User> allVotingUsers = new ArrayList<>(List.of(user1, user2));

        when(userSelectionPrioritiesRepository.findAll()).thenReturn(allPriorities);
        when(userRepository.findAllWithUserRole()).thenReturn(allVotingUsers);
        when(userRepository.countUsers()).thenReturn(2);
        when(ideaSelectionRepository.findAll()).thenReturn(List.of(ideaSelection1, ideaSelection2));

        // Act
        userSelectionService.assignUsersToGroups();

        // Assert
        verify(userSelectionResultRepository, times(2)).save(any(UserSelectionResult.class));
    }

    @Test
    void getGroupAssignments() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");

        Idea idea = new Idea();
        idea.setId(1);
        idea.setTitle("Test Idea");

        UserSelectionResult.UserSelectionResultId id = new UserSelectionResult.UserSelectionResultId(1, 1);
        UserSelectionResult result = new UserSelectionResult(id);

        when(userSelectionResultRepository.findAll()).thenReturn(List.of(result));
        when(ideaRepository.getReferenceById(1)).thenReturn(idea);
        when(userRepository.getReferenceById(1)).thenReturn(user);

        // Expected result
        GroupAssignmentsDTO expectedGroupAssignment = new GroupAssignmentsDTO("Test Idea", List.of("testUser"));

        // Act
        List<GroupAssignmentsDTO> groupAssignments = userSelectionService.getGroupAssignments();

        // Assert
        assertNotNull(groupAssignments);
        assertEquals(1, groupAssignments.size());
        assertEquals(expectedGroupAssignment, groupAssignments.get(0));

        verify(userSelectionResultRepository, times(1)).findAll();
        verify(ideaRepository, times(1)).getReferenceById(1);
        verify(userRepository, times(1)).getReferenceById(1);
    }

    @Test
    void hasUserSubmitted() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);

        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(userSelectionPrioritiesRepository.existsByUserId(1)).thenReturn(true);

        // Act
        boolean result = userSelectionService.hasUserSubmitted();

        // Assert
        assertTrue(result);
        verify(authenticationService, times(1)).getCurrentUser();
        verify(userSelectionPrioritiesRepository, times(1)).existsByUserId(1);
    }

    @Test
    void hasUserSubmittedWhenNoSubmission() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);

        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(userSelectionPrioritiesRepository.existsByUserId(1)).thenReturn(false);

        // Act
        boolean result = userSelectionService.hasUserSubmitted();

        // Assert
        assertFalse(result);
        verify(authenticationService, times(1)).getCurrentUser();
        verify(userSelectionPrioritiesRepository, times(1)).existsByUserId(1);
    }

    @Test
    void resultsPresent() {
        // Arrange
        when(userSelectionResultRepository.count()).thenReturn(5L);

        // Act
        boolean result = userSelectionService.resultsPresent();

        // Assert
        assertTrue(result);
        verify(userSelectionResultRepository, times(1)).count();
    }

    @Test
    void resultsPresentWhenNoResults() {
        // Arrange
        when(userSelectionResultRepository.count()).thenReturn(0L);

        // Act
        boolean result = userSelectionService.resultsPresent();

        // Assert
        assertFalse(result);
        verify(userSelectionResultRepository, times(1)).count();
    }

    @Test
    void preferenceChoiceActive() {
        // Arrange
        when(ideaSelectionRepository.count()).thenReturn(10L);

        // Act
        boolean result = userSelectionService.preferenceChoiceActive();

        // Assert
        assertTrue(result);
        verify(ideaSelectionRepository, times(1)).count();
    }

    @Test
    void preferenceChoiceActiveWhenNoChoices() {
        // Arrange
        when(ideaSelectionRepository.count()).thenReturn(0L);

        // Act
        boolean result = userSelectionService.preferenceChoiceActive();

        // Assert
        assertFalse(result);
        verify(ideaSelectionRepository, times(1)).count();
    }

}