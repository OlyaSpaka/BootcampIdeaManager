package com.example.demo.controllers;

import com.example.demo.models.Competition;
import com.example.demo.models.User;
import com.example.demo.services.CompetitionService;
import com.example.demo.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@PreAuthorize("hasRole('ROLE_Admin')")
@RequestMapping("/admin")
@Controller
public class AdminToolsController {

    private UserService userService;
    private CompetitionService competitionService;

    public AdminToolsController(UserService userService, CompetitionService competitionService) {
        this.userService = userService;
        this.competitionService = competitionService;
    }

    @GetMapping
    public String getAdminDashboard(Model model) {
        Competition currentCompetition = competitionService.getCompetition(1);
        User currentUser = userService.getCurrentUser();
        List<User> users = userService.getAllUsers();
        model.addAttribute("competition", currentCompetition);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "admin-dashboard"; // Returns admin-dashboard.html
    }

    @PutMapping("/admin/bootcamp/edit")



    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        User currentUser = userService.getCurrentUser();

        if(currentUser.getId() == id) {
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
