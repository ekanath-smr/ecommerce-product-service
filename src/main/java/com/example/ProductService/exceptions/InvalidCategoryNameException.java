package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidCategoryNameException extends Exception {
    private final String categoryName;
    public InvalidCategoryNameException(String message, String categoryName) {
        super(message);
        this.categoryName = categoryName;
    }
}
