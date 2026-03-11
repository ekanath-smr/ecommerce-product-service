package com.example.ProductService.controllers;

import com.example.ProductService.dtos.*;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Product;
import com.example.ProductService.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.example.ProductService.mappers.ProductMapper.mapProductToProductDto;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("productServiceImpl")  ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product by ID", description = "Fetches a product by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid product ID")
    })
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) throws InvalidProductIdException {
        Product product = productService.getProductById(productId);
        return mapProductToProductDto(product);
    }

    @Operation(summary = "Get all products with pagination", description = "Returns a paginated list of all products with sorting options.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully")
    })
    @GetMapping
    public Page<ProductDto> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<Product> products = productService.getAllProducts(page, size, sortBy, sortDirection);
        return products.map(ProductMapper::mapProductToProductDto);
    }

    @Operation(summary = "Create a new product", description = "Creates a new product with title, price, description, category, and image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or product already exists")
    })
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductRequestDto requestDto)
            throws InvalidCategoryNameException, ProductAlreadyExistException {
        Product product = productService.createProduct(requestDto.getTitle(), requestDto.getPrice(),
                requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
        return mapProductToProductDto(product);
    }

    @Operation(summary = "Update product", description = "Updates an existing product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid product ID or category")
    })
    @PatchMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequestDto requestDto)
            throws InvalidCategoryNameException, InvalidProductIdException {
        Product updatedProduct = productService.updateProduct(productId, requestDto.getTitle(), requestDto.getPrice(),
                requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
        return mapProductToProductDto(updatedProduct);
    }

    @Operation(summary = "Delete product", description = "Deletes a product by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invalid product ID")
    })
    @DeleteMapping("/{productId}")
    public boolean deleteProductById(@PathVariable Long productId) throws InvalidProductIdException {
        productService.deleteProductById(productId);
        return true;
    }

    @Operation(summary = "Search products", description = "Search products by keyword, category, and price range with pagination and sorting.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    })
    @GetMapping("/search")
    public Page<ProductDto> searchProducts(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String categoryName,
                                           @RequestParam(required = false) Double minPrice,
                                           @RequestParam(required = false) Double maxPrice,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "price") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDirection) {
        return productService.searchProducts(keyword, categoryName, minPrice, maxPrice, page, size, sortBy, sortDirection)
                .map(ProductMapper::mapProductToProductDto);
    }

//    @ExceptionHandler(InvalidProductIdException.class)
//    public ResponseEntity<String> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
//        return new ResponseEntity<>("LocalExceptionHandler: " + invalidProductIdException.getProductId() + " is an invalid product id, Please pass a valid product id", HttpStatus.NOT_FOUND);
//    }

}
