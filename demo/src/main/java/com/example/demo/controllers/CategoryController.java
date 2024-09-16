package com.example.demo.controllers;

import com.example.demo.models.Category;
import com.example.demo.services.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Category")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
    public void addCategory(@RequestBody Category category){
        categoryService.addCategory(category);
    }
    @DeleteMapping(path = "{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Integer id) {
        categoryService.deleteCategory(id);
    }
}
