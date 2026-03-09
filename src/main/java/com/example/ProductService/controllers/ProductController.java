package com.example.ProductService.controllers;

import com.example.ProductService.dtos.*;
import com.example.ProductService.dtos.ResponseStatus;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.models.Product;
import com.example.ProductService.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public ProductResponseDto getProductById(@PathVariable Long productId) throws InvalidProductIdException {
        ProductResponseDto responseDto = new ProductResponseDto();
        // For testing LocalExceptionHandler and GlobalExceptionHandler
//        try {
            Product product = productService.getProductById(productId);
            responseDto.setProduct(mapProductToProductDto(product));
            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (InvalidProductIdException e) {
//            responseDto.setProduct(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
        return responseDto;
    }

    // localhost:8080/products
    @GetMapping
    public List<ProductDto> getAllProducts() {
        // For Testing Global Exception Handler
//        throw new RuntimeException();
        List<Product> products = productService.getAllProducts();
        return mapProductListToProductDtoList(products);
    }

    // localhost:8080/products
    @PostMapping
    public ProductDto createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = new ProductResponseDto();
//        try {
            Product product = productService.createProduct(requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
            return mapProductToProductDto(product);
//            responseDto.setProduct(mapProductToProductDto(product));
//            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (ProductAlreadyExistException e) {
//            responseDto.setProduct(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
//        return responseDto;
    }

    // localhost:8080/products
    @PatchMapping("/{productId}")
    public ProductResponseDto updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = new ProductResponseDto();
        try {
            Product product = productService.updateProduct(productId, requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
            responseDto.setProduct(mapProductToProductDto(product));
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InvalidProductIdException e) {
            responseDto.setProduct(null);
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    @DeleteMapping("/{productId}")
    public boolean deleteProductById(@PathVariable Long productId) throws InvalidProductIdException {
//        try {
            productService.deleteProductById(productId);
            return true;
//        } catch (InvalidProductIdException e) {
//            return false;
//        }
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
        return new ResponseEntity<>("LocalExceptionHandler: " + invalidProductIdException.getProductId() + " is an invalid product id, Please pass a valid product id", HttpStatus.NOT_FOUND);
    }
}
