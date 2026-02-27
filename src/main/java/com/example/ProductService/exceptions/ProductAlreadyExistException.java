package com.example.ProductService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAlreadyExistException extends Exception {
    private Long productId;
    private String message;
    public ProductAlreadyExistException(String message, Long productId) {
        super(message);
        this.productId = productId;
        this.message = message;
    }
}
