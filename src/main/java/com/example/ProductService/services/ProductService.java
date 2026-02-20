package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public abstract Product getSingleProduct(Long productId) throws InvalidProductIdException;
    public abstract List<Product> getAllProduct();
    public abstract Product createProduct(Long id, String title, float price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException;
    public abstract Product replaceProduct(Long id, String title, float price, String description, String categoryName, String imageUrl) throws InvalidProductIdException;
    public abstract void deleteProduct(Long productId) throws InvalidProductIdException;
}
