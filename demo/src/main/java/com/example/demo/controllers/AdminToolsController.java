package com.example.demo.controllers;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.models.Competition;
import com.example.demo.models.User;
import com.example.demo.services.CompetitionService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@PreAuthorize("hasRole('ROLE_Admin')")
@RequestMapping("/admin")
@Controller
public class AdminToolsController {

    private final UserService userService;
    private final CompetitionService competitionService;
    private final int competitionId = 1; // hardcoded for now as we only have one bootcamp

    @Autowired
    public AdminToolsController(UserService userService, CompetitionService competitionService) {
        this.userService = userService;
        this.competitionService = competitionService;
    }

    @GetMapping
    public String getAdminDashboard(Model model) {
        Competition currentCompetition = competitionService.getCompetition(competitionId);
        User currentUser = userService.getCurrentUser();
        List<User> users = userService.getAllUsers();
        model.addAttribute("competition", currentCompetition);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "admin-dashboard";
    }

    @PutMapping("/bootcamp/update")
    public String updateBootcampDetails(@Valid @ModelAttribute CompetitionDTO competitionDTO, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("unspecifiedErrorEdit", "Form contains errors. Please try again.");
            return "redirect:/admin";
        }
        try {
            String name = competitionDTO.getName();
            String description = competitionDTO.getDescription();
            LocalDate startDate = competitionDTO.getStartDate();
            LocalDate endDate = competitionDTO.getEndDate();
            int amountOfWinners = competitionDTO.getAmountOfWinners();

            competitionService.updateCompetitionContent(competitionId, description, name);
            competitionService.updateCompetitionDate(competitionId, startDate, endDate);
            competitionService.updateCompetitionNumberOfWinners(competitionId, amountOfWinners);

            redirectAttributes.addFlashAttribute("successEdit", "Details updated successfully.");


        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("unspecifiedErrorEdit", "Something went wrong. Try again later.");
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
            return "redirect:/admin";
        }

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
        }
        return "redirect:/admin";
    }


}
