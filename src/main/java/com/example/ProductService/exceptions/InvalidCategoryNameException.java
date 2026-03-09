package com.example.ProductService.exceptions;

public class InvalidCategoryNameException extends Exception {
    public InvalidCategoryNameException(String message) {
        super(message);
    }
}
