package com.example.ProductService.controllers;

import com.example.ProductService.dtos.*;
import com.example.ProductService.dtos.ResponseStatus;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.mappers.FakeStoreProductMapper;
import com.example.ProductService.models.Product;
import com.example.ProductService.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// localhost:8080/products
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("realProductService")  ProductService productService) {
        this.productService = productService;
    }

    // localhost:8080/products/{productId}
    @GetMapping("/{productId}")
    public GetSingleProductResponseDto getSingleProduct(@PathVariable Long productId) throws InvalidProductIdException {
        GetSingleProductResponseDto responseDto = new GetSingleProductResponseDto();
        // For testing LocalExceptionHandler and GlobalExceptionHandler
//        try {
            Product product = productService.getSingleProduct(productId);
            responseDto.setProduct(product);
            responseDto.setStatus(ResponseStatus.SUCCESS);
//        } catch (InvalidProductIdException e) {
//            responseDto.setProduct(null);
//            responseDto.setStatus(ResponseStatus.FAILURE);
//        }
        return responseDto;
    }

    // localhost:8080/products
    @GetMapping
    public GetAllProductsResponseDto getAllProducts() {
        // For Testing Global Exception Handler
//        throw new RuntimeException();
        GetAllProductsResponseDto responseDto = new GetAllProductsResponseDto();
        List<Product> products = productService.getAllProduct();
        responseDto.setProducts(products);
        responseDto.setStatus(ResponseStatus.SUCCESS);
        return responseDto;
    }

    // localhost:8080/products
    @PostMapping
    public CreateProductResponseDto createProduct(@RequestBody FakeStoreProductDto requestDto) {
        CreateProductResponseDto responseDto = new CreateProductResponseDto();
        try {
            Product product = productService.createProduct(requestDto.getId(), requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
            responseDto.setProduct(FakeStoreProductMapper.mapProductToFakeStoreProductDto(product));
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (ProductAlreadyExistException e) {
            responseDto.setProduct(null);
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    // localhost:8080/products
    @PutMapping
    public ReplaceProductResponseDto replaceProduct(@RequestBody FakeStoreProductDto requestDto) {
        ReplaceProductResponseDto responseDto = new ReplaceProductResponseDto();
        try {
            Product product = productService.replaceProduct(requestDto.getId(), requestDto.getTitle(), requestDto.getPrice(), requestDto.getDescription(), requestDto.getCategory(), requestDto.getImage());
            responseDto.setProduct(FakeStoreProductMapper.mapProductToFakeStoreProductDto(product));
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InvalidProductIdException e) {
            responseDto.setProduct(null);
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    @DeleteMapping("/{productId}")
    public boolean DeleteProduct(@PathVariable Long productId) throws InvalidProductIdException {
//        try {
            productService.deleteProduct(productId);
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
