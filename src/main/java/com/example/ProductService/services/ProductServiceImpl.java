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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        log.debug("Fetching product with id={}", productId);

        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found for id={}", productId);
                    return new ProductNotFoundException("Product with this id not found", productId);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateProductById(Long productId) {
        log.debug("Validating existence of product with id={}", productId);
        return productRepository.existsById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching all products page={}, size={}, sortBy={}, sortDirection={}", page, size, sortBy, sortDirection);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findAll(pageable);
        log.debug("Fetched {} products out of total {}", products.getNumberOfElements(), products.getTotalElements());
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
        log.debug("Searching products with criteria: keyword={}, category={}, minPrice={}, maxPrice={}, page={}, size={}",
                criteria.getKeyword(), criteria.getCategoryName(),
                criteria.getMinPrice(), criteria.getMaxPrice(),
                criteria.getPage(), criteria.getSize());
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
        Page<Product> result = productRepository.findAll(spec, pageable);
        log.debug("Search returned {} products (total={})",
                result.getNumberOfElements(), result.getTotalElements());
        return result;
    }

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {
        String title = productRequestDto.getTitle();

        log.info("Creating product with title={}", title);
        Optional<Product> productOptional = productRepository.findByTitle(title);
        if(productOptional.isPresent()) {
            log.warn("Product already exists with title={}", title);
            throw new ProductAlreadyExistException("Product with title: " + title + ", already exist.", title);
        }
        Product product = new Product();
        populateProductFields(
                productRequestDto.getTitle(), productRequestDto.getPrice(), productRequestDto.getDescription(),
                productRequestDto.getCategory(), productRequestDto.getImage(), product
        );
        productRepository.save(product);
        log.info("Product created successfully id={}, title={}", product.getId(), product.getTitle());
        return product;
    }

    @Override
    public Product updateProduct(Long productId, ProductRequestDto productRequestDto) {
        log.info("Updating product with id={}", productId);
        Product product = null;
        try {
            product = getProductById(productId);
        } catch (ProductNotFoundException e) {
            log.warn("Update failed. Product not found id={}", productId);
            throw new InvalidProductIdException("Invalid product id", productId);
        }
        populateProductFields(
                productRequestDto.getTitle(), productRequestDto.getPrice(),
                productRequestDto.getDescription(), productRequestDto.getCategory(),
                productRequestDto.getImage(), product
        );
        productRepository.save(product);
        log.info("Product updated successfully id={}, title={}", productId, product.getTitle());
        return product;
    }

    @Override
    public void deleteProductById(Long productId) {
        log.warn("Deleting product with id={}", productId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) {
            log.warn("Delete failed. Product not found id={}", productId);
            throw new InvalidProductIdException("Product with id " + productId + ", doesn't exist to delete", productId);
        }
        productRepository.deleteById(productId);
        log.warn("Product deleted successfully id={}", productId);
    }

    private void populateProductFields(String title, BigDecimal price, String description, String categoryName, String imageUrl, Product product) {
        log.debug("Populating product fields for title={}", title);
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if(categoryOptional.isEmpty()) {
            log.warn("Invalid category name provided: {}", categoryName);
            throw new InvalidCategoryNameException("Invalid category name.", categoryName);
        } else {
            product.setCategory(categoryOptional.get());
        }
        product.setImageUrl(imageUrl);
        log.debug("Product fields populated successfully for title={}", title);
    }

}
