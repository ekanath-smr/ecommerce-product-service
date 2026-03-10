package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
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
        product.setPrice(1000.0);
        product.setDescription("Apple phone");
        product.setCategory(category);
    }

    @Test
    void testGetProductByIdSuccess() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals("iPhone", result.getTitle());
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidProductIdException.class, () -> productService.getProductById(1L));
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
        when(productRepository.search(any(), any(), any(), any(), any())).thenReturn(page);
        Page<Product> result = productService.searchProducts("iphone", "Electronics", 500.0, 1500.0, 0, 10, "id", "asc");
        assertEquals(1, result.getTotalElements());
        verify(productRepository).search(any(), any(), any(), any(), any());
    }

    @Test
    void testCreateProductSuccess() throws Exception {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        Product result = productService.createProduct("iPhone", 1000, "Apple phone", "Electronics", "image.jpg");
        assertEquals("iPhone", result.getTitle());
        verify(productRepository).findByTitle("iPhone");
        verify(categoryRepository).findByName("Electronics");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testCreateProductAlreadyExists() {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.of(product));
        assertThrows(ProductAlreadyExistException.class, () -> productService.createProduct("iPhone", 1000, "desc", "Electronics", "img"));
        verify(productRepository).findByTitle("iPhone");
    }

    @Test
    void testCreateProductInvalidCategory() {
        when(productRepository.findByTitle("iPhone")).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        assertThrows(InvalidCategoryNameException.class, () -> productService.createProduct("iPhone", 1000, "desc", "Electronics", "img"));
        verify(productRepository).findByTitle("iPhone");
        verify(categoryRepository).findByName("Electronics");
    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        Product updated = productService.updateProduct(1L, "iPhone 15", 1200, "New phone", "Electronics", "image.jpg");
        assertEquals("iPhone 15", updated.getTitle());
        verify(productRepository).findById(1L);
        verify(categoryRepository).findByName("Electronics");
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProductInvalidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidProductIdException.class, () -> productService.updateProduct(1L, "title", 100, "desc", "Electronics", "img"));
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
}