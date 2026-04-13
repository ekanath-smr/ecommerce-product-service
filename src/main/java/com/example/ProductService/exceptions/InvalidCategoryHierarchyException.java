package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidCategoryHierarchyException extends RuntimeException {
    private final String message;
    public InvalidCategoryHierarchyException(String message) {
        super(message);
        this.message = message;
    }
}
