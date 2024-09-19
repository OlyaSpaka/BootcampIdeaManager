package com.example.demo.services;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testuser";
        User user = new User(username, "testuser@example.com", "password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        User result = authenticationService.loadUserByUsername(username);

        // Assert
        assertEquals(user, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(username));
    }

    @Test
    void testIsUsernameTaken_UsernameExists() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        // Act
        boolean result = authenticationService.isUsernameTaken(username);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsUsernameTaken_UsernameDoesNotExist() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        boolean result = authenticationService.isUsernameTaken(username);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testIsEmailTaken_EmailExists() {
        // Arrange
        String email = "testuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act
        boolean result = authenticationService.isEmailTaken(email);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testIsEmailTaken_EmailDoesNotExist() {
        // Arrange
        String email = "nonexistentuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean result = authenticationService.isEmailTaken(email);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testRegisterNewUser() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setEmail("newuser@example.com");
        registerDTO.setPassword("password");

        Role role = new Role();
        role.setName("User");

        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedpassword");
        when(roleRepository.findByName("User")).thenReturn(Optional.of(role));

        // Act
        authenticationService.registerNewUser(registerDTO);

        // Assert
        verify(userRepository, times(1)).save(argThat(user ->
                user.getUsername().equals(registerDTO.getUsername()) &&
                        user.getEmail().equals(registerDTO.getEmail()) &&
                        user.getPassword().equals("encodedpassword") &&
                        user.getRoles().contains(role)
        ));
    }

    @Test
    void testGetCurrentUser() {
        // Arrange
        User user = new User("testuser", "testuser@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        // Act
        User result = authenticationService.getCurrentUser();

        // Assert
        assertEquals(user, result);
    }
}
