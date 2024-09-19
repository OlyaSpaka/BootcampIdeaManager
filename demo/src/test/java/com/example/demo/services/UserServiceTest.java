package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testAddUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.addUser(user);

        // Assert
        verify(userRepository, times(1)).save(user); // Verify save method is called once
    }
    @Test
    void testDeleteUser_Success() {
        // Arrange
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).existsById(userId); // Verify existence check
        verify(userRepository, times(1)).deleteById(userId); // Verify delete method is called
    }
    @Test
    void testDeleteUser_Failure() {
        // Arrange
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.deleteUser(userId));
        assertEquals("User with Id 1 does not exist", exception.getMessage());

        // Verify that deleteById is never called if user doesn't exist
        verify(userRepository, never()).deleteById(userId);
    }
}
