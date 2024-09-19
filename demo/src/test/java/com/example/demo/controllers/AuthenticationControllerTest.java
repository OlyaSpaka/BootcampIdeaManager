package com.example.demo.controllers;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@WithMockUser(username = "user", roles = {"User"})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("usernamefr");
        registerDTO.setPassword("password123");
        registerDTO.setConfirmPassword("password123");
        registerDTO.setEmail("user@example.com");

        when(authenticationService.isUsernameTaken(registerDTO.getUsername())).thenReturn(false);
        when(authenticationService.isEmailTaken(registerDTO.getEmail())).thenReturn(false);
//        doNothing().when(authenticationService).registerNewUser(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword())
                        .param("email", registerDTO.getEmail())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/register"))
                .andExpect(flash().attributeExists("success"));

        verify(authenticationService, times(1)).registerNewUser(any(RegisterDTO.class));
    }

    @Test
    public void testRegisterUserValidationFailure() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("user");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("differentpassword");
        registerDTO.setEmail("user@example.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword())
                        .param("email", registerDTO.getEmail())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/register"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.user"))
                .andExpect(flash().attributeExists("user"));

        verify(authenticationService, never()).registerNewUser(any(RegisterDTO.class));
    }
}
