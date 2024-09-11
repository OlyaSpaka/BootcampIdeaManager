package com.example.demo.auth;

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
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    public void registerNewUser(User user) throws Exception {
        System.out.println("Saving user: " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            System.out.println("Username already exists");
            throw new Exception("Username already exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            System.out.println("Email already exists");
            throw new Exception("Email already exists");
        }

        userRepository.save(user);
    }
}
