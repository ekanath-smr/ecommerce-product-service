package com.example.ProductService.services;

import com.example.ProductService.exceptions.CategoryAlreadyExistsException;
import com.example.ProductService.exceptions.InvalidCategoryIdException;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.models.Category;
import org.springframework.data.domain.Page;

//@Service
public interface CategoryService {
    Category createCategory(String name, String description) throws CategoryAlreadyExistsException;
    Category updateCategory(Long categoryId, String name, String description) throws InvalidCategoryIdException;
    void deleteCategoryById(Long categoryId) throws InvalidCategoryIdException;
//    List<Category> getAllCategories();
    Page<Category> getAllCategories(int page, int size, String sortBy, String sortDirection);
    Category getCategoryById(Long categoryId) throws InvalidCategoryIdException;
    Category getCategoryByName(String name) throws InvalidCategoryNameException;
}
