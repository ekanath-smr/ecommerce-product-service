package com.example.ProductService.exceptions;

public class CannotDeleteCategoryException extends RuntimeException {
    public CannotDeleteCategoryException(String message) {
        super(message);
    }
}
