package com.example.ProductService.services;

import com.example.ProductService.dtos.ProductRequestDto;
import com.example.ProductService.dtos.ProductSearchCriteria;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.exceptions.ProductNotFoundException;
import com.example.ProductService.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

//@Service
public interface ProductService {
    Product createProduct(ProductRequestDto productRequestDto);
    Product updateProduct(Long productId, ProductRequestDto productRequestDto);
    void deleteProductById(Long productId);
    Product getProductById(Long productId);
    Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection);
    Page<Product> searchProducts(ProductSearchCriteria productSearchCriteria);
}
