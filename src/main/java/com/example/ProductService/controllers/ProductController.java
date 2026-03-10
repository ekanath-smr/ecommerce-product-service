package com.example.ProductService.controllers;

import com.example.ProductService.dtos.*;
import com.example.ProductService.dtos.ResponseStatus;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Product;
import com.example.ProductService.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ProductService.mappers.ProductMapper.mapProductToProductDto;
import static com.example.ProductService.mappers.ProductMapper.mapProductListToProductDtoList;

// localhost:8080/products
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("productServiceImpl")  ProductService productService) {
        this.productService = productService;
    }

    // localhost:8080/products/{productId}
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) throws InvalidProductIdException {
        Product product = productService.getProductById(productId);
        return mapProductToProductDto(product);
    }

    // localhost:8080/products
    @GetMapping
    public Page<ProductDto> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection) {
//        // For Testing Global Exception Handler
//        throw new RuntimeException();
        Page<Product> products = productService.getAllProducts(page, size, sortBy, sortDirection);
        return products.map(ProductMapper::mapProductToProductDto);
    }

    // localhost:8080/products
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductRequestDto requestDto) throws InvalidCategoryNameException, ProductAlreadyExistException {
        Product product = productService.createProduct(requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
        return mapProductToProductDto(product);
    }

    // localhost:8080/products
    @PatchMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequestDto requestDto) throws InvalidCategoryNameException, InvalidProductIdException {
        Product updatedProduct = productService.updateProduct(productId, requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
        return mapProductToProductDto(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public boolean deleteProductById(@PathVariable Long productId) throws InvalidProductIdException {
        productService.deleteProductById(productId);
        return true;
    }

    @GetMapping("/search")
    public Page<ProductDto> searchProducts(@RequestParam(required = false) String keyword, @RequestParam(required = false) String categoryName,
                                        @RequestParam(required = false) Double minPrice,@RequestParam(required = false) Double maxPrice,
                                        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "price") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection) {
        return productService.searchProducts(keyword, categoryName, minPrice, maxPrice, page, size, sortBy, sortDirection)
                .map(ProductMapper::mapProductToProductDto);
    }

//    @ExceptionHandler(InvalidProductIdException.class)
//    public ResponseEntity<String> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
//        return new ResponseEntity<>("LocalExceptionHandler: " + invalidProductIdException.getProductId() + " is an invalid product id, Please pass a valid product id", HttpStatus.NOT_FOUND);
//    }
}
