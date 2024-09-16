package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AuthenticationController {

    private final UserRegistrationService userRegistrationService;

    public AuthenticationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());  // Empty User object for the form
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        // Check for existing username
        if (userRegistrationService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.username", "Username already exists!");
        }

        // Check for existing email
        if (userRegistrationService.emailExists(user.getEmail())) {
            result.rejectValue("email", "error.email", "Email already exists!");
        }


        if (result.hasErrors()) {
            return "register"; // Return to form with validation errors
        }

        try {
            userRegistrationService.registerNewUser(user);
            model.addAttribute("success", "Registration successful!");
            return "redirect:/login"; // Redirect to login page
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";

        }
    }

}
