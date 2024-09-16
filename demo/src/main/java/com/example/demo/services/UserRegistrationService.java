package com.example.demo.services;

import com.example.demo.exceptions.EmailTakenException;
import com.example.demo.exceptions.UsernameTakenException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Check if username already exists
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Check if email already exists
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void registerNewUser(User user) throws UsernameTakenException, EmailTakenException {
        System.out.println("Saving user: " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (isUsernameTaken(user.getUsername())) {
            System.out.println("Username already exists");
            throw new UsernameTakenException("Username already exists");
        }

        if (isEmailTaken(user.getEmail())) {
            System.out.println("Email already exists");
            throw new EmailTakenException("Email already exists");
        }

        userRepository.save(user);
    }
}
