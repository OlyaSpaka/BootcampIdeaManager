package com.example.demo.mapper.implementation;

import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.models.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void testMapCategoryToOutputCategoryDTO() {
        Category category = new Category();
        category.setId(1);
        category.setName("Technology");

        OutputCategoryDTO dto = categoryMapper.map(category);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("Technology", dto.getName());
    }

    @Test
    void testMapInputCategoryDTOToCategory() {
        InputCategoryDTO inputDTO = new InputCategoryDTO();
        inputDTO.setName("Health");

        Category category = categoryMapper.map(inputDTO);

        assertNotNull(category);
        assertEquals("Health", category.getName());
    }

    @Test
    void testMapCategoryListToOutputCategoryDTOList() {
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Sports");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Education");

        List<Category> categories = List.of(category1, category2);
        List<OutputCategoryDTO> dtos = categoryMapper.map(categories);

        assertEquals(2, dtos.size());
        assertEquals("Sports", dtos.get(0).getName());
        assertEquals("Education", dtos.get(1).getName());
    }
}
