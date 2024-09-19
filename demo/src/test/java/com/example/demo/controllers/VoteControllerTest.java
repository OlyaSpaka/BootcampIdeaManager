package com.example.demo.controllers;

import com.example.demo.dto.general.VoteDTO;
import com.example.demo.models.Vote;
import com.example.demo.services.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@WithMockUser(username = "user", roles = {"User"})
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddVote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/Vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"value\"}")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(voteService, times(1)).addVote(any(VoteDTO.class));
    }

    @Test
    public void testDeleteVote() throws Exception {
        int voteId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/Vote/{voteId}", voteId)
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(voteService, times(1)).deleteVote(voteId);
    }

    @Test
    public void testShowPoints() throws Exception {
        int ideaId = 1;
        int points = 10;
        when(voteService.calculatePoints(ideaId)).thenReturn(points);

        mockMvc.perform(MockMvcRequestBuilders.get("/Vote")
                        .param("ideaId", String.valueOf(ideaId)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(points)));
    }
}
