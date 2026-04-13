package com.example.ProductService.services;

import com.example.ProductService.dtos.ProductRequestDto;
import com.example.ProductService.dtos.ProductSearchCriteria;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.exceptions.ProductNotFoundException;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.repositories.CategoryRepository;
import com.example.ProductService.repositories.ProductRepository;
import com.example.ProductService.specifications.ProductSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

// Built robust Product Service layer for e-commerce platform supporting product CRUD, paginated retrieval,
// dynamic filtering/search via JPA Specifications, category association validation, duplicate product prevention, and centralized exception handling.

@Service("productServiceImpl")
@Primary
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        logger.debug("Fetching product with id: {}", productId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            logger.warn("Product with id {} not found", productId);
//            throw new InvalidProductIdException("Invalid product id", productId);
            throw new ProductNotFoundException("Product with this id not found", productId);
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

//    This way is not scalable, there can be new filter criteria added in the future, we cannot change the custom query every time we add a filter criteria in the future.
//    @Override
//    @Transactional(readOnly = true)
//    public Page<Product> searchProducts(ProductSearchCriteria productSearchCriteria) {
//        logger.debug("Searching products keyword={}, category={}, price={}, to={}",
//                productSearchCriteria.getKeyword(), productSearchCriteria.getCategoryName(),
//                productSearchCriteria.getMinPrice(), productSearchCriteria.getMaxPrice()
//        );
//        Sort sort = Sort.by(
//                Sort.Direction.fromString(productSearchCriteria.getSortDirection()),
//                productSearchCriteria.getSortBy()
//        );
//        Pageable pageable = PageRequest.of(productSearchCriteria.getPage(), productSearchCriteria.getSize(), sort);
//        return productRepository.search(
//                productSearchCriteria.getKeyword(), productSearchCriteria.getCategoryName(),
//                productSearchCriteria.getMinPrice(), productSearchCriteria.getMaxPrice(), pageable
//        );
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(ProductSearchCriteria criteria) {
        logger.debug(
                "Searching products keyword={}, category={}, minPrice={}, maxPrice={}",
                criteria.getKeyword(), criteria.getCategoryName(),
                criteria.getMinPrice(), criteria.getMaxPrice()
        );
        Sort sort = Sort.by(
                Sort.Direction.fromString(criteria.getSortDirection()),
                criteria.getSortBy()
        );
        Pageable pageable = PageRequest.of(
                criteria.getPage(), criteria.getSize(), sort
        );
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasKeyword(criteria.getKeyword()))
                .and(ProductSpecification.hasCategory(criteria.getCategoryName()))
                .and(ProductSpecification.minPrice(criteria.getMinPrice()))
                .and(ProductSpecification.maxPrice(criteria.getMaxPrice()));
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {
        String title = productRequestDto.getTitle();

        logger.info("Creating product: {}", title);
        Optional<Product> productOptional = productRepository.findByTitle(title);
        if(productOptional.isPresent()) {
            logger.warn("Product already exists with title: {}", title);
            throw new ProductAlreadyExistException("Product with title: " + title + ", already exist.", title);
        }
        Product product = new Product();
        populateProductFields(
                productRequestDto.getTitle(), productRequestDto.getPrice(), productRequestDto.getDescription(),
                productRequestDto.getCategory(), productRequestDto.getImage(), product
        );
        productRepository.save(product);
        logger.info("Product created successfully with id: {}", product.getId());
        return product;
    }

    @Override
    public Product updateProduct(Long productId, ProductRequestDto productRequestDto) {
        logger.info("Updating product id: {}", productId);
        Product product = null;
        try {
            product = getProductById(productId);
        } catch (ProductNotFoundException e) {
            throw new InvalidProductIdException("Invalid product id", productId);
        }
        populateProductFields(
                productRequestDto.getTitle(), productRequestDto.getPrice(),
                productRequestDto.getDescription(), productRequestDto.getCategory(),
                productRequestDto.getImage(), product
        );
        productRepository.save(product);
        logger.info("Product updated successfully: {}", productId);
        return product;
    }

    @Override
    public void deleteProductById(Long productId) {
        logger.warn("Deleting product with id: {}", productId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            throw new InvalidProductIdException("Product with id " + productId + ", doesn't exist to delete", productId);
        }
        productRepository.deleteById(productId);
        logger.warn("Product deleted successfully: {}", productId);
    }

    private void populateProductFields(String title, BigDecimal price, String description, String categoryName, String imageUrl, Product product) {
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if(categoryOptional.isEmpty()) {
            logger.warn("Category not found: {}", categoryName);
            throw new InvalidCategoryNameException("Invalid category name.", categoryName);
        } else {
            product.setCategory(categoryOptional.get());
        }
        product.setImageUrl(imageUrl);
    }

}
