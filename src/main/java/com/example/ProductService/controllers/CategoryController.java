package com.example.ProductService.controllers;

import com.example.ProductService.dtos.CategoryDto;
import com.example.ProductService.dtos.CategoryRequestDto;
import com.example.ProductService.dtos.CategoryResponseDto;
import com.example.ProductService.exceptions.CategoryAlreadyExistsException;
import com.example.ProductService.exceptions.InvalidCategoryIdException;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.mappers.CategoryMapper;
import com.example.ProductService.models.Category;
import com.example.ProductService.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ProductService.mappers.CategoryMapper.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create Category
    @Operation(summary = "Create a new category", description = "Creates a new category with a name and description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request / Category already exists")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequestDto requestDto) throws CategoryAlreadyExistsException {
        logger.info("Received request to create category with name: {}", requestDto.getName());
        Category category = categoryService.createCategory(requestDto);
        logger.info("Category created successfully with id: {}", category.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryMapper.mapCategoryToCategoryDto(category));
    }

    // Get All Categories with Pagination
    @Operation(summary = "Get all categories with pagination", description = "Returns a paginated categories with sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories fetched successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        logger.debug("Fetching categories page={}, size={}, sortBy={}, direction={}", page, size, sortBy, sortDirection);
        Page<CategoryDto> result = categoryService.getAllCategories(page, size, sortBy, sortDirection).map(CategoryMapper::mapCategoryToCategoryDto);
        return ResponseEntity.ok(result);
    }

    // Get Category by ID
    @Operation(summary = "Get category by ID", description = "Fetches a category by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) throws InvalidCategoryIdException {
        logger.debug("Fetching category with id: {}", id);
        Category category = categoryService.getCategoryById(id);
        logger.debug("Category fetched successfully for id: {}", id);
        return ResponseEntity.ok(
                CategoryMapper.mapCategoryToCategoryDto(category)
        );
//        CategoryResponseDto responseDto = new CategoryResponseDto();
//        responseDto.setCategory(mapCategoryToCategoryDto(category));
//        responseDto.setStatus(ResponseStatus.SUCCESS);
//        return responseDto;
    }

    // Get Category by Name
    @Operation(summary = "Get category by name", description = "Fetches a category by its unique name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping(params = "name")
    public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam("name") String name) throws InvalidCategoryNameException {
        logger.debug("Fetching category with name: {}", name);
        Category category = categoryService.getCategoryByName(name);
        logger.debug("Category fetched successfully for name: {}", name);
        return ResponseEntity.ok(
                CategoryMapper.mapCategoryToCategoryDto(category)
        );
//        CategoryResponseDto responseDto = new CategoryResponseDto();
//        responseDto.setCategory(mapCategoryToCategoryDto(category));
//        responseDto.setStatus(ResponseStatus.SUCCESS);
//        return responseDto;
    }

    // Update Category
    @Operation(summary = "Update category", description = "Updates category details (name and description) by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto requestDto) throws InvalidCategoryIdException {
        logger.info("Updating category with id: {}", id);
        Category updatedCategory = categoryService.updateCategory(id, requestDto);
        logger.info("Category updated successfully with id: {}", id);
        return ResponseEntity.ok(
                CategoryMapper.mapCategoryToCategoryDto(updatedCategory)
        );
//        CategoryResponseDto responseDto = new CategoryResponseDto();
//        responseDto.setCategory(mapCategoryToCategoryDto(updatedCategory));
//        responseDto.setStatus(ResponseStatus.SUCCESS);
//        return responseDto;
    }

    // Delete Category
    @Operation(summary = "Delete category", description = "Deletes a category by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long id) throws InvalidCategoryIdException {
        logger.warn("Deleting category with id: {}", id);
        categoryService.deleteCategoryById(id);
        logger.warn("Category deleted successfully with id: {}", id);
//        return true;
    }
}