package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidCategoryIdException extends Exception {
    private final Long categoryId;
    public InvalidCategoryIdException(String message, Long categoryId) {
        super(message);
        this.categoryId = categoryId;
    }
}
