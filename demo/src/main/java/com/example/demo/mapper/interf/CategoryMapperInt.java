package com.example.demo.mapper.interf;

import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.models.Category;

import java.util.List;

public interface CategoryMapperInt {
    Category map(InputCategoryDTO categoryDTO);
    OutputCategoryDTO map(Category category);
    List<OutputCategoryDTO> map(List<Category> categoryList);
}
