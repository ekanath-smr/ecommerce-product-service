package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("realProductService")
//@Primary
public class RealProductService implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public RealProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long productId) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Invalid product id", productId);
        }
        return productOptional.get();
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Long id, String title, double price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()) {
            throw new ProductAlreadyExistException("Product with id " + id + ", already exist", id);
        }
        Product product = new Product();
//        if(id != null) {
//            product.setId(id);
//        }
        return getProduct(title, price, description, categoryName, imageUrl, product);
    }

    @NonNull
    private Product getProduct(String title, double price, String description, String categoryName, String imageUrl, Product product) {
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        Category category;
        if(categoryOptional.isEmpty()) {
            category = new Category();
            category.setName(categoryName);
            categoryRepository.save(category);
        } else {
            category = categoryOptional.get();
        }
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product replaceProduct(Long id, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with id " + id + ", doesn't exist to replace", id);
        }
        // delete + recreate != update
//        deleteProduct(id);
//        try {
//            return createProduct(id, title, price, description, categoryName, imageUrl);
//        } catch (ProductAlreadyExistException e) {
//            return null;
//        }
        // update = fetch + edit + save
        Product product = productOptional.get();
        return getProduct(title, price, description, categoryName, imageUrl, product);
    }

    @Override
    public void deleteProduct(Long productId) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with id " + productId + ", doesn't exist to delete", productId);
        }
        productRepository.deleteById(productId);
    }
}
