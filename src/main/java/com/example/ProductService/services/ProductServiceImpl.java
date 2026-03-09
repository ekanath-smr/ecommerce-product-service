package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;
//import org.jspecify.annotations.NonNull;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productServiceImpl")
@Primary
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long productId) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Invalid product id", productId);
        }
        return productOptional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, double price, String description, String categoryName, String imageUrl) {
//        Optional<Product> productOptional = productRepository.findById(id);
//        if(productOptional.isPresent()) {
//            throw new ProductAlreadyExistException("Product with id " + id + ", already exist", id);
//        }
        Product product = new Product();
//        if(id != null) {
//            product.setId(id);
//        }
        getProduct(title, price, description, categoryName, imageUrl, product);
        productRepository.save(product);
        return product;
    }

    @NotNull
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
        return product;
    }

    @Override
    public Product updateProduct(Long productId, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with productId " + productId + ", doesn't exist to replace", productId);
        }
        // delete + recreate != update
//        deleteProduct(productId);
//        try {
//            return createProduct(productId, title, price, description, categoryName, imageUrl);
//        } catch (ProductAlreadyExistException e) {
//            return null;
//        }
        // update = fetch + edit + save
        Product product = productOptional.get();
        product = getProduct(title, price, description, categoryName, imageUrl, product);
        productRepository.save(product);
        return product;
    }

    @Override
    public void deleteProductById(Long productId) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with id " + productId + ", doesn't exist to delete", productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return List.of();
    }
}
