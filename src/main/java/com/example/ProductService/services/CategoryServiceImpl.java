package com.example.ProductService.services;


import com.example.ProductService.exceptions.CategoryAlreadyExistsException;
import com.example.ProductService.exceptions.InvalidCategoryIdException;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.models.Category;
import com.example.ProductService.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
//    @Transactional
    public Category createCategory(String name, String description) throws CategoryAlreadyExistsException {
        logger.info("Attempting to create category with name: {}", name);
        try {
            getCategoryByName(name);
            logger.warn("Category already exists with name: {}", name);
            throw new CategoryAlreadyExistsException("Category already exists.", name);
        } catch (InvalidCategoryNameException e) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
            logger.info("Category created successfully with id: {}", category.getId());
            return category;
        }
    }

    @Override
//    @Transactional
    public Category updateCategory(Long categoryId, String name, String description) throws InvalidCategoryIdException {
        logger.info("Updating category with id: {}", categoryId);
        Category category = getCategoryById(categoryId);
        category.setName(name);
        category.setDescription(description);
        categoryRepository.save(category);
        logger.info("Category updated successfully with id: {}", categoryId);
        return category;
    }

    @Override
//    @Transactional
    public void deleteCategoryById(Long categoryId) throws InvalidCategoryIdException {
        logger.warn("Deleting category with id: {}", categoryId);
        Category category = getCategoryById(categoryId);
        categoryRepository.deleteById(categoryId);
        logger.warn("Category deleted successfully with id: {}", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(int page, int size, String sortBy, String sortDirection) {
        logger.debug("Fetching categories page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
//        Pageable pageable = PageRequest.of(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        logger.debug("Fetched {} categories", categories.getNumberOfElements());
        return categories;
    }

//    @Override
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long categoryId) throws InvalidCategoryIdException {
        logger.debug("Fetching category with id: {}", categoryId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isEmpty()) {
            logger.warn("Category not found with id: {}", categoryId);
            throw new InvalidCategoryIdException("Invalid category id.");
        }
        return categoryOptional.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) throws InvalidCategoryNameException {
        logger.debug("Fetching category with name: {}", name);
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if(categoryOptional.isEmpty()) {
            logger.debug("Category not found with name: {}", name);
            throw new InvalidCategoryNameException("Invalid category name.");
        }
        return categoryOptional.get();
    }
}
