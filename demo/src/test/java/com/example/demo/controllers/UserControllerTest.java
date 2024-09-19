package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testAddUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/User")
                        .contentType("application/json")
                        .content("{\"id\":1,\"username\":\"newuser\",\"email\":\"newuser@example.com\"}")
                        .with(csrf())) // Adjust JSON content based on your User model
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testDeleteUser() throws Exception {
        int userId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/User/{userId}", userId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }
}
