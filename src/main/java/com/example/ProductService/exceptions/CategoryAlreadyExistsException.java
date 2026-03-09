package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class CategoryAlreadyExistsException extends Exception {
    private final String name;
    public CategoryAlreadyExistsException(String message, String name) {
        super(message);
        this.name = name;
    }
}
