package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class CategoryAlreadyExistsException extends RuntimeException {
    private final String message;
    private final String name;
    public CategoryAlreadyExistsException(String message, String name) {
        super(message);
        this.message = message;
        this.name = name;
    }
}
