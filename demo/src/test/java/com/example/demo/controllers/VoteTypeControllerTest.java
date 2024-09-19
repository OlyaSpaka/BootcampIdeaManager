package com.example.demo.controllers;

import com.example.demo.models.VoteType;
import com.example.demo.services.VoteTypeService;
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

@WebMvcTest(VoteTypeController.class)
@WithMockUser(username = "user", roles = {"User"})
public class VoteTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteTypeService voteTypeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddVoteType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/VoteType")
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"Type A\"}")
                        .with(csrf())) // Adjust JSON content based on your VoteType model
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(voteTypeService, times(1)).addVoteType(any(VoteType.class));
    }

    @Test
    public void testDeleteVoteType() throws Exception {
        int voteTypeId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/VoteType/{voteTypeId}", voteTypeId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(voteTypeService, times(1)).deleteVoteType(voteTypeId);
    }
}
