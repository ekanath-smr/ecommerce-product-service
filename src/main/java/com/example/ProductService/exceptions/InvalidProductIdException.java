package com.example.ProductService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidProductIdException extends Exception {
    private String message;
    private Long productId;
    public InvalidProductIdException(String message, Long productId) {
        super(message);
        this.message = message;
        this.productId = productId;
    }
}
