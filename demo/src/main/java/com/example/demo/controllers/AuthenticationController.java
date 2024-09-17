package com.example.demo.controllers;

import com.example.demo.exceptions.EmailTakenException;
import com.example.demo.exceptions.UsernameTakenException;
import com.example.demo.models.User;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/auth")
@Controller
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")){
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/auth/register"; // Return to form with validation errors
        }
        try {
            authenticationService.registerNewUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/auth/login";
        }  catch (UsernameTakenException e) {
            redirectAttributes.addFlashAttribute("usernameError", e.getMessage());
        } catch (EmailTakenException e) {
            redirectAttributes.addFlashAttribute("emailError", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/auth/register";
    }
}
