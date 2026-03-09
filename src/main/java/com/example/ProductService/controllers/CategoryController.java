package com.example.ProductService.controllers;

import com.example.ProductService.dtos.CategoryDto;
import com.example.ProductService.dtos.CategoryRequestDto;
import com.example.ProductService.dtos.CategoryResponseDto;
import com.example.ProductService.dtos.ResponseStatus;
import com.example.ProductService.exceptions.CategoryAlreadyExistsException;
import com.example.ProductService.exceptions.InvalidCategoryIdException;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.mappers.CategoryMapper;
import com.example.ProductService.models.Category;
import com.example.ProductService.services.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ProductService.mappers.CategoryMapper.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(@Qualifier("categoryServiceImpl") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create Category
    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryRequestDto requestDto) throws CategoryAlreadyExistsException {
        logger.info("Received request to create category with name: {}", requestDto.getName());
        Category category = categoryService.createCategory(requestDto.getName(), requestDto.getDescription());
        logger.info("Category created successfully with id: {}", category.getId());
        return mapCategoryToCategoryDto(category);
    }

//    // Get All Categories
//    @GetMapping
//    public List<CategoryDto> getAllCategories() {
//        List<Category> categories = categoryService.getAllCategories();
//        return mapCategoryListToCategoryDtoList(categories);
//    }

    // Get All Categories
    @GetMapping
    public Page<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection) {
        logger.debug("Fetching categories page={}, size={}, sortBy={}, direction={}", page, size, sortBy, sortDirection);
        return categoryService.getAllCategories(page, size, sortBy, sortDirection).map(CategoryMapper::mapCategoryToCategoryDto);
    }

    // Get Category by ID
    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) throws InvalidCategoryIdException {
        logger.debug("Fetching category with id: {}", id);
        CategoryResponseDto responseDto = new CategoryResponseDto();
//        try {
            Category category = categoryService.getCategoryById(id);
            responseDto.setCategory(mapCategoryToCategoryDto(category));
            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (InvalidCategoryIdException e) {
//            responseDto.setCategory(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
        logger.debug("Category fetched successfully for id: {}", id);
        return responseDto;
    }

    @GetMapping(params = "name")
    public CategoryResponseDto getCategoryByName(@RequestParam("name") String name) throws InvalidCategoryNameException {
        logger.debug("Fetching category with name: {}", name);
        CategoryResponseDto responseDto = new CategoryResponseDto();
//        try {
            Category category = categoryService.getCategoryByName(name);
            responseDto.setCategory(mapCategoryToCategoryDto(category));
            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (InvalidCategoryNameException e) {
//            responseDto.setCategory(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
        logger.debug("Category fetched successfully for name: {}", name);
        return responseDto;
    }

    // Update Category
    @PatchMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto requestDto) throws InvalidCategoryIdException {
        logger.info("Updating category with id: {}", id);
        CategoryResponseDto responseDto = new CategoryResponseDto();
//        try {
            Category updatedCategory = categoryService.updateCategory(id, requestDto.getName(), requestDto.getDescription());
            responseDto.setCategory(mapCategoryToCategoryDto(updatedCategory));
            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (InvalidCategoryIdException e) {
//            responseDto.setCategory(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
        logger.info("Category updated successfully with id: {}", id);
        return responseDto;
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public boolean deleteCategoryById(@PathVariable Long id) throws InvalidCategoryIdException {
        logger.warn("Deleting category with id: {}", id);
//        try {
            categoryService.deleteCategoryById(id);
            logger.warn("Category deleted successfully with id: {}", id);
            return true;
//        } catch (InvalidCategoryIdException e) {
//            return false;
//        }
    }
}
