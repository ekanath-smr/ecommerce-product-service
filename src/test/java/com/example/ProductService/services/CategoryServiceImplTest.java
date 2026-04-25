package com.example.ProductService.services;

import com.example.ProductService.dtos.CategoryRequestDto;
import com.example.ProductService.exceptions.*;
import com.example.ProductService.models.Category;
import com.example.ProductService.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Achieved comprehensive unit test coverage for hierarchical category business logic including parent-child validation,
// circular dependency prevention, deletion constraints, and duplicate detection using JUnit & Mockito.

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;
    private Category parentCategory;

    @BeforeEach
    void setUp() {
        parentCategory = new Category();
        parentCategory.setId(10L);
        parentCategory.setName("Parent");

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
        category1.setDescription("Electronic items");
        category1.setSubCategories(new ArrayList<>());

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Clothing");
        category2.setDescription("Clothes");
        category2.setSubCategories(new ArrayList<>());
    }

    // ================= CREATE =================

    @Test
    void createCategory_shouldCreateSuccessfully_withoutParent() {
        when(categoryRepository.findByName("NewCategory")).thenReturn(Optional.empty());

        Category saved = new Category();
        saved.setName("NewCategory");
        saved.setDescription("Desc");

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryService.createCategory(
                CategoryRequestDto.builder()
                        .name("NewCategory")
                        .description("Desc")
                        .build()
        );

        assertEquals("NewCategory", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_shouldCreateSuccessfully_withParentId() {
        when(categoryRepository.findByName("Mobiles")).thenReturn(Optional.empty());
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(parentCategory));

        Category result = categoryService.createCategory(
                CategoryRequestDto.builder()
                        .name("Mobiles")
                        .description("Phones")
                        .parentCategoryId(10L)
                        .build()
        );

        assertEquals(parentCategory, result.getParentCategory());
        verify(categoryRepository).findById(10L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_shouldThrow_whenCategoryAlreadyExists() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(
                        CategoryRequestDto.builder()
                                .name("Electronics")
                                .build()
                ));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createCategory_shouldThrow_whenInvalidParentId() {
        when(categoryRepository.findByName("Mobiles")).thenReturn(Optional.empty());
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(InvalidParentCategoryId.class,
                () -> categoryService.createCategory(
                        CategoryRequestDto.builder()
                                .name("Mobiles")
                                .parentCategoryId(999L)
                                .build()
                ));
    }

    @Test
    void createCategory_shouldThrow_whenBothParentIdAndNameProvided() {
        when(categoryRepository.findByName("Mobiles")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.createCategory(
                        CategoryRequestDto.builder()
                                .name("Mobiles")
                                .parentCategoryId(1L)
                                .parentCategoryName("Electronics")
                                .build()
                ));
    }

    // ================= UPDATE =================

    @Test
    void updateCategory_shouldUpdateSuccessfully() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findByName("NewName")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        Category result = categoryService.updateCategory(
                1L,
                CategoryRequestDto.builder()
                        .name("NewName")
                        .description("Updated")
                        .build()
        );

        assertEquals("NewName", result.getName());
        assertEquals("Updated", result.getDescription());
    }

    @Test
    void updateCategory_shouldThrow_whenDuplicateNameExists() {
        when(categoryRepository.findByName("Clothing")).thenReturn(Optional.of(category2));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category2));

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(
                        1L,
                        CategoryRequestDto.builder()
                                .name("Clothing")
                                .build()
                ));
    }

    @Test
    void updateCategory_shouldThrow_whenSelfParentAssigned() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        assertThrows(InvalidParentAssignmentException.class,
                () -> categoryService.updateCategory(
                        1L,
                        CategoryRequestDto.builder()
                                .name("Electronics")
                                .parentCategoryId(1L)
                                .build()
                ));
    }

    @Test
    void updateCategory_shouldThrow_whenCircularHierarchyDetected() {
        category1.setParentCategory(category2);
        category2.setParentCategory(category1);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));

        assertThrows(InvalidCategoryHierarchyException.class,
                () -> categoryService.updateCategory(
                        1L,
                        CategoryRequestDto.builder()
                                .name("Electronics")
                                .parentCategoryId(2L)
                                .build()
                ));
    }

    // ================= DELETE =================

    @Test
    void deleteCategory_shouldDeleteSuccessfully() {
        category1.setSubCategories(new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_shouldThrow_whenHasChildren() {
        category1.setSubCategories(List.of(category2));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        assertThrows(CannotDeleteParentCategoryException.class,
                () -> categoryService.deleteCategoryById(1L));

        verify(categoryRepository, never()).deleteById(anyLong());
    }

    // ================= GET ALL =================

    @Test
    void getAllCategories_shouldReturnPaginatedResult() {
        Page<Category> page = new PageImpl<>(List.of(category1, category2));

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Category> result = categoryService.getAllCategories(0, 10, "id", "ASC");

        assertEquals(2, result.getContent().size());
    }

    // ================= GET BY ID =================

    @Test
    void getCategoryById_shouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryById_shouldThrow_whenInvalidId() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryIdException.class,
                () -> categoryService.getCategoryById(999L));
    }

    // ================= GET BY NAME =================

    @Test
    void getCategoryByName_shouldReturnCategory() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));

        Category result = categoryService.getCategoryByName("Electronics");

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryByName_shouldThrow_whenInvalidName() {
        when(categoryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryNameException.class,
                () -> categoryService.getCategoryByName("Unknown"));
    }
}