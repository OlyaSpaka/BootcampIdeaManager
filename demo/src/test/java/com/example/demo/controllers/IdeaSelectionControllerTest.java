package com.example.demo.controllers;

import com.example.demo.models.IdeaSelection;
import com.example.demo.services.IdeaSelectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(IdeaSelectionController.class)
public class IdeaSelectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaSelectionService ideaSelectionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testAddIdeaSelection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/IdeaSelection")
                        .contentType("application/json")
                        .content("{\"id\":1}")
                        .with(csrf())) // Adjust JSON content based on your IdeaSelection model
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(ideaSelectionService, times(1)).addIdeaSelection(any(IdeaSelection.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testDeleteIdeaSelection() throws Exception {
        int ideaSelectionId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/IdeaSelection/{ideaSelectionId}", ideaSelectionId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(ideaSelectionService, times(1)).deleteIdeaSelection(ideaSelectionId);
    }
}
