package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.services.RoleService;
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

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testAddRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/Role")
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"Admin\"}")
                        .with(csrf())) // Adjust JSON content based on your Role model
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(roleService, times(1)).addRole(any(Role.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"Admin"})
    public void testDeleteRole() throws Exception {
        int roleId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/Role/{roleId}", roleId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(roleService, times(1)).deleteRole(roleId);
    }
}
