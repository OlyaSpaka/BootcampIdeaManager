package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
}
