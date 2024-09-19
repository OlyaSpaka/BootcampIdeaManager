package com.example.demo.controllers;

import com.example.demo.models.Category;
import com.example.demo.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(CategoryController.class)
@WithMockUser(username = "user", roles = {"User"})
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCategory() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");

        mockMvc.perform(post("/Category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).addCategory(category);
    }

    @Test
    public void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/Category/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(1);
    }

    @Test
    public void testUpdateCategoryName() throws Exception {
        mockMvc.perform(put("/Category/1/name")
                        .param("name", "Updated Category")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).updateCategoryName(1, "Updated Category");
    }
}
