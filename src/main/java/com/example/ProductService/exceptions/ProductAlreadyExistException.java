package com.example.ProductService.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAlreadyExistException extends RuntimeException {
    private String title;
    public ProductAlreadyExistException(String message, String title) {
        super(message);
        this.title = title;
    }
}
