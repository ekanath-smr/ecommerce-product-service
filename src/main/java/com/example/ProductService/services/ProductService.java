package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

//@Service
public interface ProductService {
    Product createProduct(String title, double price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException, InvalidCategoryNameException;
    Product updateProduct(Long productId, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException, InvalidCategoryNameException;
    void deleteProductById(Long productId) throws InvalidProductIdException;
    Product getProductById(Long productId) throws InvalidProductIdException;
    Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection);
    Page<Product> searchProducts(String keyword, String categoryName, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDirection);
}
