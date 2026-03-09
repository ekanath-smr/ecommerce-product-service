package com.example.ProductService.exceptions;

public class InvalidCategoryIdException extends Exception {
    public InvalidCategoryIdException(String message) {
        super(message);
    }
}
