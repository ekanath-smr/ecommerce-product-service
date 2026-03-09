package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.models.Product;

import java.util.List;

//@Service
public interface ProductService {
    public abstract Product getProductById(Long productId) throws InvalidProductIdException;
    public abstract List<Product> getAllProducts();
    public abstract Product createProduct(String title, double price, String description, String categoryName, String imageUrl);
    public abstract Product updateProduct(Long productId, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException;
    public abstract void deleteProductById(Long productId) throws InvalidProductIdException;
    public abstract List<Product> searchProducts(String keyword);
}
