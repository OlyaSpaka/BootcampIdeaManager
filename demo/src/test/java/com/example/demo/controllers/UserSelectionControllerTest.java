package com.example.demo.controllers;

import com.example.demo.dto.GroupAssignmentsDTO;
import com.example.demo.dto.UserSelectionPrioritiesDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.models.User;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.CompetitionService;
import com.example.demo.services.IdeaService;
import com.example.demo.services.UserSelectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserSelectionControllerTest {

    @Mock
    private UserSelectionService userSelectionService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CompetitionService competitionService;

    @Mock
    private IdeaService ideaService;

    @InjectMocks
    private UserSelectionController userSelectionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userSelectionController).build();
    }

    @Test
    void submitPreferences() throws Exception {
        // Arrange
        UserSelectionPrioritiesDTO mockDTO = new UserSelectionPrioritiesDTO(1,
                "2024-09-19T22:34:49Z", Map.of(1, 1));

        String mockRequestBody = """
                {
                    "userId": 1,
                    "priorities": { "1": 1 },
                    "submissionTime": "2024-09-19T22:34:49Z"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/user-selection/submit-preferences")
                        .contentType("application/json")
                        .content(mockRequestBody))
                .andExpect(status().isOk())
                .andExpect(view().name("preferences-waiting-page"))
                .andExpect(model().attributeExists("userId"));

        // Verify that the service method was called with the correct arguments
        ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Map> prioritiesCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Timestamp> timestampCaptor = ArgumentCaptor.forClass(Timestamp.class);

        verify(userSelectionService).saveUserSelections(userIdCaptor.capture(), prioritiesCaptor.capture(), timestampCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(1);
        assertThat(prioritiesCaptor.getValue()).isEqualTo(Map.of(1, 1));
        assertThat(timestampCaptor.getValue()).isEqualTo(Timestamp.from(Instant.parse("2024-09-19T22:34:49Z")));
    }

    @Test
    void submitPreferencesErrorHandling() throws Exception {
        // Simulate an exception being thrown by the service
        doThrow(new RuntimeException("Test Exception")).when(userSelectionService).saveUserSelections(anyInt(), anyMap(), any(Timestamp.class));

        String mockRequestBody = """
                {
                    "userId": 1,
                    "priorities": { "1": 1 },
                    "submissionTime": "2024-09-19T22:34:49Z"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/user-selection/submit-preferences")
                        .contentType("application/json")
                        .content(mockRequestBody))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}
