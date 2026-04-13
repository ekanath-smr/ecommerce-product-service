package com.example.ProductService.services;

import com.example.ProductService.dtos.CategoryRequestDto;
import com.example.ProductService.exceptions.*;
import com.example.ProductService.models.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Category createCategory(CategoryRequestDto categoryRequestDto);
    Category updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto);
    void deleteCategoryById(Long categoryId);
    Page<Category> getAllCategories(int page, int size, String sortBy, String sortDirection);
    Category getCategoryById(Long categoryId);
    Category getCategoryByName(String name);
}
