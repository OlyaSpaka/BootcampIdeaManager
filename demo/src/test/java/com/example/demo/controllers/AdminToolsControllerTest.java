package com.example.demo.controllers;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.models.Competition;
import com.example.demo.models.User;
import com.example.demo.services.CompetitionService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminToolsController.class)
@WithMockUser(username = "user", roles = {"Admin"})
public class AdminToolsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CompetitionService competitionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "Admin")
    public void testGetAdminDashboard() throws Exception {
        Competition competition = new Competition();
        competition.setId(1);
        competition.setName("Bootcamp");

        User currentUser = new User();
        currentUser.setId(1);

        when(competitionService.getCompetition(1)).thenReturn(competition);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(get("/admin").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-dashboard"))
                .andExpect(model().attribute("competition", competition))
                .andExpect(model().attribute("currentUser", currentUser))
                .andExpect(model().attribute("users", Collections.singletonList(new User())));
    }

    @Test
    @WithMockUser(roles = "Admin")
    public void testUpdateBootcampDetailsSuccess() throws Exception {
        CompetitionDTO competitionDTO = new CompetitionDTO();
        competitionDTO.setName("Updated Bootcamp");
        competitionDTO.setDescription("Updated description");
        competitionDTO.setStartDate(LocalDate.of(2024, 1, 1));
        competitionDTO.setEndDate(LocalDate.of(2024, 12, 31));
        competitionDTO.setAmountOfWinners(10);

        mockMvc.perform(put("/admin/bootcamp/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", competitionDTO.getName())
                        .param("description", competitionDTO.getDescription())
                        .param("startDate", competitionDTO.getStartDate().toString())
                        .param("endDate", competitionDTO.getEndDate().toString())
                        .param("amountOfWinners", String.valueOf(competitionDTO.getAmountOfWinners()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("successEdit"));

        verify(competitionService, times(1)).updateCompetitionContent(1, competitionDTO.getDescription(), competitionDTO.getName());
        verify(competitionService, times(1)).updateCompetitionDate(1, competitionDTO.getStartDate(), competitionDTO.getEndDate());
        verify(competitionService, times(1)).updateCompetitionNumberOfWinners(1, competitionDTO.getAmountOfWinners());
    }

    @Test
    @WithMockUser(roles = "Admin")
    public void testUpdateBootcampDetailsFailure() throws Exception {
        CompetitionDTO competitionDTO = new CompetitionDTO();
        competitionDTO.setName("");
        competitionDTO.setDescription("");
        competitionDTO.setStartDate(LocalDate.now());
        competitionDTO.setEndDate(LocalDate.now());
        competitionDTO.setAmountOfWinners(-1); // Invalid amount

        mockMvc.perform(put("/admin/bootcamp/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", competitionDTO.getName())
                        .param("description", competitionDTO.getDescription())
                        .param("startDate", competitionDTO.getStartDate().toString())
                        .param("endDate", competitionDTO.getEndDate().toString())
                        .param("amountOfWinners", String.valueOf(competitionDTO.getAmountOfWinners()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("unspecifiedErrorEdit"));

        verify(competitionService, never()).updateCompetitionContent(anyInt(), anyString(), anyString());
        verify(competitionService, never()).updateCompetitionDate(anyInt(), any(LocalDate.class), any(LocalDate.class));
        verify(competitionService, never()).updateCompetitionNumberOfWinners(anyInt(), anyInt());
    }

    @Test
    @WithMockUser(roles = "Admin")
    public void testDeleteUserSuccess() throws Exception {
        User userToDelete = new User();
        userToDelete.setId(2);

        User currentUser = new User();
        currentUser.setId(1);

        when(userService.getCurrentUser()).thenReturn(currentUser);

        mockMvc.perform(delete("/admin/users/delete/2").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("success"));

        verify(userService, times(1)).deleteUser(2);
    }

    @Test
    @WithMockUser(roles = "Admin")
    public void testDeleteCurrentUserFailure() throws Exception {
        User currentUser = new User();
        currentUser.setId(1);

        when(userService.getCurrentUser()).thenReturn(currentUser);

        mockMvc.perform(delete("/admin/users/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("error"));

        verify(userService, never()).deleteUser(anyInt());
    }
}
