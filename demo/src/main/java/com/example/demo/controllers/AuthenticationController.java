package com.example.demo.controllers;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
            model.addAttribute("user", new RegisterDTO());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterDTO registerDTO, BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if(authenticationService.isUsernameTaken(registerDTO.getUsername())) {
            result.addError(new FieldError("user", "username", "Username already in use.")); }

        if(authenticationService.isEmailTaken(registerDTO.getEmail())) {
            result.addError(new FieldError("user", "email", "Email already in use.")); }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", registerDTO);
            return "redirect:/auth/register"; // Return to form with validation errors
        }

        try {
            authenticationService.registerNewUser(registerDTO);
            redirectAttributes.addFlashAttribute("success", "Registration was successful!");
            return "redirect:/auth/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("unspecifiederror", "Something went wrong. Try again later.");
        }

        redirectAttributes.addFlashAttribute("user", registerDTO);
        return "redirect:/auth/register";

    }
}
