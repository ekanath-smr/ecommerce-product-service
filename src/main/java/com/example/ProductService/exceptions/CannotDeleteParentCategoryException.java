package com.example.ProductService.exceptions;

public class CannotDeleteParentCategoryException extends RuntimeException {
    public CannotDeleteParentCategoryException(String message) {
        super(message);
    }
}
