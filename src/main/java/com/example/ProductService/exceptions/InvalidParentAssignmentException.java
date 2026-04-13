package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidParentAssignmentException extends RuntimeException {
    private final String message;
    public InvalidParentAssignmentException(String message) {
        super(message);
        this.message = message;
    }
}
