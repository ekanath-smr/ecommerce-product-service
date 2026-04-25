package com.example.ProductService.services;

import com.example.ProductService.dtos.ProductRequestDto;
import com.example.ProductService.dtos.ProductSearchCriteria;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.exceptions.ProductNotFoundException;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setTitle("iPhone");
        product.setPrice(BigDecimal.valueOf(1000.0));
        product.setDescription("Apple phone");
        product.setCategory(category);
    }

    @Test
    void testGetProductByIdSuccess() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(1L);
        assertNotNull(result);
        assertAll(
                () -> assertEquals("iPhone", result.getTitle()),
                () -> assertEquals(BigDecimal.valueOf(1000.0), result.getPrice()),
                () -> assertEquals("Electronics", result.getCategory().getName())
        );
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Product> result = productService.getAllProducts(0, 10, "id", "asc");
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void testSearchProducts() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(Pageable.class)
        )).thenReturn(page);
        ProductSearchCriteria productSearchCriteria = ProductSearchCriteria
                .builder()
                .keyword("iphone").categoryName("Electronics")
                .minPrice(BigDecimal.valueOf(500.0)).maxPrice(BigDecimal.valueOf(1500.0))
                .page(0).size(10).sortBy("id").sortDirection("asc").build();
        Page<Product> result = productService.searchProducts(productSearchCriteria);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(Pageable.class)
        );
    }

    @Test
    void testCreateProductSuccess() throws Exception {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        ProductRequestDto productRequestDto = ProductMapper.getProductRequestDtoFrom("iPhone", BigDecimal.valueOf(1000), "Apple phone", "Electronics", "image.jpg");
        Product result = productService.createProduct(productRequestDto);
        assertAll(
                () -> assertEquals("iPhone", result.getTitle()),
                () -> assertEquals(BigDecimal.valueOf(1000), result.getPrice()),
                () -> assertEquals("Electronics", result.getCategory().getName())
        );
        verify(productRepository).findByTitle("iPhone");
        verify(categoryRepository).findByName("Electronics");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testCreateProductAlreadyExists() {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.of(product));
        ProductRequestDto productRequestDto = ProductMapper.getProductRequestDtoFrom("iPhone", BigDecimal.valueOf(1000), "desc", "Electronics", "img");
        assertThrows(ProductAlreadyExistException.class, () -> productService.createProduct(productRequestDto));
        verify(productRepository).findByTitle("iPhone");
    }

    @Test
    void testCreateProductInvalidCategory() {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        ProductRequestDto productRequestDto = ProductMapper.getProductRequestDtoFrom("iPhone", BigDecimal.valueOf(1000), "desc", "Electronics", "img");
        assertThrows(InvalidCategoryNameException.class, () -> productService.createProduct(productRequestDto));
        verify(productRepository).findByTitle("iPhone");
        verify(categoryRepository).findByName("Electronics");
    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        ProductRequestDto productRequestDto = ProductMapper.getProductRequestDtoFrom("iPhone 15", BigDecimal.valueOf(1200), "New phone", "Electronics", "image.jpg");
        Product updated = productService.updateProduct(1L, productRequestDto);
        assertEquals("iPhone 15", updated.getTitle());
        verify(productRepository).findById(1L);
        verify(categoryRepository).findByName("Electronics");
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProductInvalidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        ProductRequestDto productRequestDto = ProductMapper.getProductRequestDtoFrom("title", BigDecimal.valueOf(100), "desc", "Electronics", "img");
//      assertThrows(InvalidProductIdException.class, () -> productService.updateProduct(1L, "title", 100, "desc", "Electronics", "img"));
        assertThrows(InvalidProductIdException.class, () -> productService.updateProduct(1L, productRequestDto));
        verify(productRepository).findById(1L);
    }

    @Test
    void testDeleteProductSuccess() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProductById(1L);
        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProductInvalidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidProductIdException.class, () -> productService.deleteProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    void testValidateProductByIdTrue() {
        when(productRepository.existsById(1L)).thenReturn(true);
        boolean result = productService.validateProductById(1L);
        assertTrue(result);
        verify(productRepository).existsById(1L);
    }

    @Test
    void testValidateProductByIdFalse() {
        when(productRepository.existsById(1L)).thenReturn(false);
        boolean result = productService.validateProductById(1L);
        assertFalse(result);
    }
}