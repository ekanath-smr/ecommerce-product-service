package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidCategoryNameException extends RuntimeException {
    private final String categoryName;
    public InvalidCategoryNameException(String message, String categoryName) {
        super(message);
        this.categoryName = categoryName;
    }
}
