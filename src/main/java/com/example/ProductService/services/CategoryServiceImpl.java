package com.example.ProductService.services;


import com.example.ProductService.dtos.CategoryRequestDto;
import com.example.ProductService.exceptions.*;
import com.example.ProductService.models.Category;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Designed and implemented hierarchical category management for e-commerce backend with parent-child relationships, circular dependency prevention,
// deletion constraints, validation, pagination, sorting, and centralized exception handling using Spring Boot & JPA.

@Transactional
@Service("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Category createCategory(CategoryRequestDto categoryRequestDto) {
        String name = categoryRequestDto.getName();
        logger.info("Attempting to create category with name: {}", name);
        if (categoryRepository.findByName(name).isPresent()) {
            logger.warn("Category already exists with name: {}", name);
            throw new CategoryAlreadyExistsException("Category already exists.", name);
        }
        Category parentCategory = findParentCategoryIfExistEitherByNameOrId(categoryRequestDto.getParentCategoryName(), categoryRequestDto.getParentCategoryId());
        Category category = Category.builder().name(name).description(categoryRequestDto.getDescription()).parentCategory(parentCategory).build();
        categoryRepository.save(category);
        logger.info("Category created successfully with id: {}", category.getId());
        return category;
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
        logger.info("Updating category with id: {}", categoryId);
        Category category = getCategoryById(categoryId);
        Category parentCategory = findParentCategoryIfExistEitherByNameOrId(categoryRequestDto.getParentCategoryName(), categoryRequestDto.getParentCategoryId());
        Optional<Category> existing = categoryRepository.findByName(categoryRequestDto.getName());
        if (existing.isPresent() && !existing.get().getId().equals(categoryId)) {
            logger.warn("Category update failed - duplicate name='{}' for categoryId={}", categoryRequestDto.getName(), categoryId);
            throw new CategoryAlreadyExistsException("Category with the requested update name already exist", existing.get().getName());
        }
        if(parentCategory != null && parentCategory.getId().equals(categoryId)) {
            logger.warn("Invalid parent assignment - categoryId={} cannot be its own parent", categoryId);
            throw new InvalidParentAssignmentException("Category cannot be its own parent");
        }
        logger.debug("Updating category fields for id={} -> newName='{}'", categoryId, categoryRequestDto.getName());
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        validateNoCircularParent(category, parentCategory);
        category.setParentCategory(parentCategory);
        categoryRepository.save(category);
        logger.info("Category updated successfully with id={} name={}", categoryId, categoryRequestDto.getName());
        return category;
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        logger.info("Deleting category with id={}", categoryId);
        Category category = getCategoryById(categoryId);
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            int childCount = category.getSubCategories().size();
            logger.warn("Delete failed for categoryId={} - has {} child categories", categoryId, childCount);
            throw new CannotDeleteParentCategoryException("Cannot delete category because it has " + childCount + " child categories");
        }
        boolean hasProducts = productRepository.existsByCategory_Id(categoryId);
        if (hasProducts) {
            throw new CannotDeleteCategoryException("Cannot delete category. It has associated products.");
        }
        categoryRepository.delete(category);
        logger.info("Category deleted successfully with id={}", categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(int page, int size, String sortBy, String sortDirection) {
        logger.debug("Fetching categories page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        logger.debug("Fetched {} categories", categories.getNumberOfElements());
        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long categoryId) {
        logger.debug("Fetching category with id: {}", categoryId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isEmpty()) {
            logger.warn("Category not found for id={}", categoryId);
            throw new InvalidCategoryIdException("Invalid category id.", categoryId);
        }
        Category category = categoryOptional.get();
        logger.debug("Category fetched successfully id={}, name='{}'", category.getId(), category.getName());
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryByName(String name) {
        logger.debug("Fetching category with name: {}", name);
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if(categoryOptional.isEmpty()) {
            logger.warn("Category not found for name='{}'", name);
            throw new InvalidCategoryNameException("Invalid category name.", name);
        }
        Category category = categoryOptional.get();
        logger.debug("Category fetched successfully id={}, name='{}'", category.getId(), category.getName());
        return category;
    }

    private Category findParentCategoryIfExistEitherByNameOrId(String parentCategoryName, Long parentCategoryId) {
        logger.debug("Resolving parent category. parentCategoryId={}, parentCategoryName={}", parentCategoryId, parentCategoryName);
        if (parentCategoryId != null && parentCategoryName != null) {
            logger.warn("Both parentCategoryId={} and parentCategoryName={} provided. Only one allowed.", parentCategoryId, parentCategoryName);
            throw new IllegalArgumentException("Provide either parentCategoryId or parentCategoryName, not both");
        }
        Category parentCategory = null;
        if(parentCategoryId != null) {
            Optional<Category> parentCategoryOptional = categoryRepository.findById(parentCategoryId);
            if (parentCategoryOptional.isPresent()) {
                parentCategory = parentCategoryOptional.get();
            } else {
                logger.warn("Invalid parent category ID: {}", parentCategoryId);
                throw new InvalidParentCategoryId("Invalid parent category id", parentCategoryId);
            }
        } else if(parentCategoryName != null) {
            Optional<Category> parentCategoryOptional = categoryRepository.findByName(parentCategoryName);
            if (parentCategoryOptional.isPresent()) {
                parentCategory = parentCategoryOptional.get();
            } else {
                logger.warn("Invalid parent category name: {}", parentCategoryName);
                throw new InvalidParentCategoryName("Invalid parent category name", parentCategoryName);
            }
        }
        return parentCategory;
    }

    private void validateNoCircularParent(Category category, Category parentCategory) {
        Category current = parentCategory;
        while (current != null) {
            if (current.getId().equals(category.getId())) {
                logger.warn("Circular hierarchy detected for categoryId={}", category.getId());
                throw new InvalidCategoryHierarchyException("Circular category hierarchy detected");
            }
            current = current.getParentCategory();
        }
    }
}
