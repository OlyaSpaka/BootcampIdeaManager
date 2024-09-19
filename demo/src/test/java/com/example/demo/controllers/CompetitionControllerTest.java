package com.example.demo.controllers;

import com.example.demo.models.Competition;
import com.example.demo.services.CompetitionService;
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

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CompetitionController.class)
public class CompetitionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompetitionService competitionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testAddCompetition() throws Exception {
        Competition competition = new Competition();
        competition.setId(1);
        competition.setName("New Competition");
        competition.setDescription("Description of the competition");

        mockMvc.perform(MockMvcRequestBuilders.post("/Competition")
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"New Competition\",\"description\":\"Description of the competition\"}")
                        .with(csrf())) // Adjust JSON content based on your Competition model
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(competitionService, times(1)).addCompetition(any(Competition.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testDeleteCompetition() throws Exception {
        int competitionId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/Competition/{competitionId}", competitionId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(competitionService, times(1)).deleteCompetition(competitionId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testUpdateCompetitionContent() throws Exception {
        int competitionId = 1;

        mockMvc.perform(MockMvcRequestBuilders.put("/Competition/{competitionId}/content", competitionId)
                        .param("description", "Updated description")
                        .param("title", "Updated title")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(competitionService, times(1)).updateCompetitionContent(competitionId, "Updated description", "Updated title");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testUpdateCompetitionDate() throws Exception {
        int competitionId = 1;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        mockMvc.perform(MockMvcRequestBuilders.put("/Competition/{competitionId}/date", competitionId)
                        .param("start", startDate.toString())
                        .param("end", endDate.toString())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(competitionService, times(1)).updateCompetitionDate(competitionId, startDate, endDate);
    }
}
