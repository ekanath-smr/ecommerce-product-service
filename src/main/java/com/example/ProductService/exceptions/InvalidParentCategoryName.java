package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidParentCategoryName extends RuntimeException {
    private final String message;
    private final String parentCategoryName;
    public InvalidParentCategoryName(String message, String parentCategoryName) {
        super(message);
        this.message = message;
        this.parentCategoryName = parentCategoryName;
    }
}
