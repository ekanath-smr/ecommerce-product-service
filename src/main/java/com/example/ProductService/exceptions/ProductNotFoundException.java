package com.example.ProductService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundException extends RuntimeException {
    private String message;
    private Long productId;
    public ProductNotFoundException(String message, Long productId) {
        super(message);
        this.message = message;
        this.productId = productId;
    }
}