package com.example.ProductService.exceptions;

public class ProductAlreadyExistException extends Exception {
    public ProductAlreadyExistException(String message) {
        super(message);
    }
}
