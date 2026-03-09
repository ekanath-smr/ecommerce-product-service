package com.example.ProductService.services;

import com.example.ProductService.exceptions.CategoryAlreadyExistsException;
import com.example.ProductService.exceptions.InvalidCategoryIdException;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.models.Category;
import com.example.ProductService.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
        category1.setDescription("Electronic items");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Clothing");
        category2.setDescription("Clothes");
    }

    //================ CREATE =================

    @Test
    void createCategory_shouldCreateCategorySuccessfully() throws CategoryAlreadyExistsException, InvalidCategoryNameException {
        when(categoryRepository.findByName("NewCategory")).thenReturn(Optional.empty());
        Category newCategory = new Category();
        newCategory.setName("NewCategory");
        newCategory.setDescription("Desc");
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        Category created = categoryService.createCategory("NewCategory", "Desc");

        assertEquals("NewCategory", created.getName());
        assertEquals("Desc", created.getDescription());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_shouldThrowException_whenCategoryAlreadyExists() throws InvalidCategoryNameException {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory("Electronics", "Desc"));

        verify(categoryRepository, never()).save(any());
    }

    //================ UPDATE =================

    @Test
    void updateCategory_shouldUpdateCategorySuccessfully() throws InvalidCategoryIdException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        Category updated = categoryService.updateCategory(1L, "NewName", "NewDesc");

        assertEquals("NewName", updated.getName());
        assertEquals("NewDesc", updated.getDescription());
        verify(categoryRepository, times(1)).save(category1);
    }

    @Test
    void updateCategory_shouldThrowException_whenIdInvalid() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryIdException.class,
                () -> categoryService.updateCategory(99L, "Name", "Desc"));

        verify(categoryRepository, never()).save(any());
    }

    //================ DELETE =================

    @Test
    void deleteCategoryById_shouldDeleteCategorySuccessfully() throws InvalidCategoryIdException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategoryById_shouldThrowException_whenIdInvalid() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryIdException.class,
                () -> categoryService.deleteCategoryById(99L));

        verify(categoryRepository, never()).deleteById(anyLong());
    }

    //================ GET ALL =================

    @Test
    void getAllCategories_shouldReturnPaginatedCategories() {
        List<Category> categoryList = Arrays.asList(category1, category2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Category> page = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(page);

        Page<Category> result = categoryService.getAllCategories(0, 10, "id", "ASC");

        assertEquals(2, result.getContent().size());
        assertEquals("Electronics", result.getContent().get(0).getName());
    }

    //================ GET BY ID =================

    @Test
    void getCategoryById_shouldReturnCategory() throws InvalidCategoryIdException {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryById_shouldThrowException_whenIdInvalid() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryIdException.class,
                () -> categoryService.getCategoryById(99L));
    }

    //================ GET BY NAME =================

    @Test
    void getCategoryByName_shouldReturnCategory() throws InvalidCategoryNameException {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category1));

        Category result = categoryService.getCategoryByName("Electronics");

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryByName_shouldThrowException_whenNameInvalid() {
        when(categoryRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(InvalidCategoryNameException.class,
                () -> categoryService.getCategoryByName("NonExistent"));
    }
}