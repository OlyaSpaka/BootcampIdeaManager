package com.example.demo.services;

import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.mapper.implementation.CategoryMapper;
import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id){
        boolean exists = categoryRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Category with Id " + id + " does not exist");
        } else {
            categoryRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateCategoryName(Integer id,
                                   String name) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "Category with Id " + id + " does not exist."));
        if (name != null && !name.isEmpty()) {
            category.setName(name);
        }
    }

    public List<OutputCategoryDTO> getAllCategories() {
        return categoryMapper.map(categoryRepository.findAll());
    }
}
