package com.example.demo.mapper.implementation;

import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.mapper.interf.CategoryMapperInt;
import com.example.demo.models.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryMapper implements CategoryMapperInt {
    @Override
    public OutputCategoryDTO map(Category category) {
        if (category == null) return null;

        OutputCategoryDTO dto = new OutputCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    @Override
    public Category map(InputCategoryDTO inputCategoryDTO) {
        if (inputCategoryDTO == null) return null;

        Category category = new Category();
        category.setName(inputCategoryDTO.getName());

        return category;
    }

    @Override
    public List<OutputCategoryDTO> map(List<Category> categoryList){
        if (categoryList == null || categoryList.isEmpty()) {
            return new ArrayList<>();  // Return an empty list instead of null
        }

        List<OutputCategoryDTO> resultList = new ArrayList<>();
        for (Category category : categoryList){
            if (category == null) continue;

            OutputCategoryDTO dto = new OutputCategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());

            resultList.add(dto);
        }
        return resultList;
    }
}
