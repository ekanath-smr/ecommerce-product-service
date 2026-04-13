package com.example.ProductService.controllers;

import com.example.ProductService.dtos.ProductDto;
import com.example.ProductService.dtos.ProductRequestDto;
import com.example.ProductService.dtos.ProductSearchCriteria;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Product;
import com.example.ProductService.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID", description = "Fetches a product by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable @Positive Long productId) {
        logger.debug("Fetching product with id: {}", productId);

        Product product = productService.getProductById(productId);

        logger.debug("Product fetched successfully for id: {}", productId);

        return ResponseEntity.ok(
                ProductMapper.mapProductToProductDto(product)
        );
    }

    @Operation(summary = "Get all products with pagination",
            description = "Returns a paginated list of all products with sorting options.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        logger.debug("Fetching products page={}, size={}, sortBy={}, direction={}",
                page, size, sortBy, sortDirection);

        Page<ProductDto> result = productService
                .getAllProducts(page, size, sortBy, sortDirection)
                .map(ProductMapper::mapProductToProductDto);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Create a new product",
            description = "Creates a new product with title, price, description, category, and image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input / Product already exists")
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        logger.info("Creating product with title: {}", productRequestDto.getTitle());

        Product product = productService.createProduct(productRequestDto);

        logger.info("Product created successfully with id: {}", product.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductMapper.mapProductToProductDto(product));
    }

    @Operation(summary = "Update product",
            description = "Updates an existing product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable @Positive Long productId,
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        logger.info("Updating product with id: {}", productId);

        Product updatedProduct = productService.updateProduct(productId, productRequestDto);

        logger.info("Product updated successfully with id: {}", productId);

        return ResponseEntity.ok(
                ProductMapper.mapProductToProductDto(updatedProduct)
        );
    }

    @Operation(summary = "Delete product",
            description = "Deletes a product by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable @Positive Long productId) {

        logger.warn("Deleting product with id: {}", productId);

        productService.deleteProductById(productId);

        logger.warn("Product deleted successfully with id: {}", productId);
    }

    @Operation(summary = "Search products",
            description = "Search products by keyword, category, and price range with pagination and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(required = false) String keyword, @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) java.math.BigDecimal minPrice, @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "price") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection) {

        logger.debug("Searching products with filters keyword={}, category={}, minPrice={}, maxPrice={}",
                keyword, categoryName, minPrice, maxPrice);

        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .keyword(keyword)
                .categoryName(categoryName)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Page<ProductDto> result = productService
                .searchProducts(criteria)
                .map(ProductMapper::mapProductToProductDto);

        return ResponseEntity.ok(result);
    }
}