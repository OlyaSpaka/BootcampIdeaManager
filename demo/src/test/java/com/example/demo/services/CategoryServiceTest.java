package com.example.demo.services;

import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.mapper.implementation.CategoryMapper;
import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory() {
        // Arrange
        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        categoryService.addCategory(category);

        // Assert
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        Integer id = 1;
        when(categoryRepository.existsById(id)).thenReturn(true);

        // Act
        categoryService.deleteCategory(id);

        // Assert
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCategory_NotFound() {
        // Arrange
        Integer id = 1;
        when(categoryRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(id));
        assertEquals("Category with Id " + id + " does not exist", exception.getMessage());

        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateCategoryName() {
        // Arrange
        Integer id = 1;
        Category category = new Category();
        category.setName("Old Name");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        // Act
        categoryService.updateCategoryName(id, "New Name");

        // Assert
        assertEquals("New Name", category.getName());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateCategoryName_NotFound() {
        // Arrange
        Integer id = 1;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> categoryService.updateCategoryName(id, "New Name"));
        assertEquals("Category with Id " + id + " does not exist.", exception.getMessage());

        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Category> categories = List.of(new Category(), new Category());
        List<OutputCategoryDTO> categoryDTOs = List.of(new OutputCategoryDTO(), new OutputCategoryDTO());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.map(categories)).thenReturn(categoryDTOs);

        // Act
        List<OutputCategoryDTO> result = categoryService.getAllCategories();

        // Assert
        assertEquals(categoryDTOs.size(), result.size());  // Ensure size matches
        assertEquals(categoryDTOs, result);  // Ensure content matches
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).map(categories);  // Ensure map is called once with the list
    }
}
