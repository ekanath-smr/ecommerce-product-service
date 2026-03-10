package com.example.ProductService.services;

import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("productServiceImpl")
@Primary
@Transactional
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryService categoryService;
    CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
//        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) throws InvalidProductIdException {
        logger.debug("Fetching product with id: {}", productId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            logger.warn("Product with id {} not found", productId);
            throw new InvalidProductIdException("Invalid product id", productId);
        }
        return productOptional.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        logger.debug("Fetching all products page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        logger.debug("Fetched {} products", products.getNumberOfElements());
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, String categoryName, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDirection) {
        logger.debug("Searching products keyword={}, category={}, price={}, to={}", keyword, categoryName, minPrice, maxPrice);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.search(keyword, categoryName, minPrice, maxPrice, pageable);
    }

//    @Override
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    @Override
    public Product createProduct(String title, double price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException, InvalidCategoryNameException {
        logger.info("Creating product: {}", title);
        Optional<Product> productOptional = productRepository.findByTitle(title);
        if(productOptional.isPresent()) {
            logger.warn("Product already exists with title: {}", title);
            throw new ProductAlreadyExistException("Product with title: " + title + ", already exist.", title);
        }
//        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
//        if(categoryOptional.isEmpty()) {
//            logger.warn("Category not found: {}", categoryName);
//            throw new InvalidCategoryNameException("Invalid category name.");
//        }
        Product product = new Product();
        getProduct(title, price, description, categoryName, imageUrl, product);
        productRepository.save(product);
        logger.info("Product created successfully with id: {}", product.getId());
        return product;
    }

    private void getProduct(String title, double price, String description, String categoryName, String imageUrl, Product product) throws InvalidCategoryNameException {
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if(categoryOptional.isEmpty()) {
            logger.warn("Category not found: {}", categoryName);
            throw new InvalidCategoryNameException("Invalid category name.", categoryName);
        } else {
            product.setCategory(categoryOptional.get());
            product.setImageUrl(imageUrl);
        }
    }

    @Override
    public Product updateProduct(Long productId, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException, InvalidCategoryNameException {
        logger.info("Updating product id: {}", productId);
        Product product = getProductById(productId);
//        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
//        if(categoryOptional.isEmpty()) {
//            logger.warn("Category not found: {}", categoryName);
//            throw new InvalidCategoryNameException("Invalid category name.");
//        }
        getProduct(title, price, description, categoryName, imageUrl, product);
        productRepository.save(product);
        logger.info("Product updated successfully: {}", productId);
        return product;
    }

    @Override
    public void deleteProductById(Long productId) throws InvalidProductIdException {
        logger.warn("Deleting product with id: {}", productId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with id " + productId + ", doesn't exist to delete", productId);
        }
        productRepository.deleteById(productId);
        logger.warn("Product deleted successfully: {}", productId);
    }

//    @Override
//    public List<Product> searchProducts(String keyword) {
//        return List.of();
//    }
}
