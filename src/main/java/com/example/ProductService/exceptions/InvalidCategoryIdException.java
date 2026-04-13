package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidCategoryIdException extends RuntimeException {
    private final Long categoryId;
    public InvalidCategoryIdException(String message, Long categoryId) {
        super(message);
        this.categoryId = categoryId;
    }
}
