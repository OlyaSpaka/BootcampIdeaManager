package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Programming");

        categoryRepository.save(category);
    }

    @Test
    void addCategory() {
        Category testCategory = new Category();
        testCategory.setName("Arts");

        categoryService.addCategory(testCategory);

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }

    @Test
    void deleteCategoryWhenExists() {
        Category categoryToDelete = new Category();
        categoryToDelete.setName("this will be deleted");
        categoryRepository.save(categoryToDelete);
        categoryService.deleteCategory(categoryToDelete.getId());
        List<Category> categoriesAfter = categoryRepository.findAll();
        assertThat(categoriesAfter).doesNotContain(categoryToDelete);
    }

    @Test
    void DeleteCategoryWhenNotExists() {
        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(999));

    }

    @Test
    void testUpdateCategoryWhenExists() {
        // When
        categoryService.updateCategoryName(category.getId(), "This is updated category");

        // Then
        Category updatedCategory = categoryRepository.findById(category.getId()).orElseThrow();
        assertThat(updatedCategory.getName()).isEqualTo("This is updated category");
    }

    @Test
    void testUpdateCategoryWhenNotExists() {
        assertThrows(IllegalStateException.class, () -> categoryService.updateCategoryName(9999, "invalid category"));
    }

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

}
