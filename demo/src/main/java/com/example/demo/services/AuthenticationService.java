package com.example.demo.services;

import com.example.demo.exceptions.EmailTakenException;
import com.example.demo.exceptions.UsernameTakenException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isEmailTaken(String email) {
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
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}




