package com.example.demo.controllers;

import com.example.demo.dto.UserSelectionPrioritiesDTO;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserSelectionControllerTest {

    @Mock
    private UserSelectionService userSelectionService;

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

        ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Map> prioritiesCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Timestamp> timestampCaptor = ArgumentCaptor.forClass(Timestamp.class);

        verify(userSelectionService).saveUserSelections(userIdCaptor.capture(), prioritiesCaptor.capture(),
                timestampCaptor.capture());

        assertThat(userIdCaptor.getValue()).isEqualTo(1);
        assertThat(prioritiesCaptor.getValue()).isEqualTo(Map.of(1, 1));
        assertThat(timestampCaptor.getValue()).isEqualTo(Timestamp.from(Instant.parse("2024-09-19T22:34:49Z")));
    }

    @Test
    void submitPreferencesErrorHandling() throws Exception {
        doThrow(new RuntimeException("Test Exception")).when(userSelectionService).saveUserSelections(anyInt(), anyMap(),
                any(Timestamp.class));

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
