package com.example.demo.auth;

import jakarta.validation.Valid;
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
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService) {
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
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes, Model model) throws Exception {

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
