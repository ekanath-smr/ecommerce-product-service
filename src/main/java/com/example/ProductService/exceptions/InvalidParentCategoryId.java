package com.example.ProductService.exceptions;

import lombok.Getter;

@Getter
public class InvalidParentCategoryId extends RuntimeException {
    private final String message;
    private final Long parentCategoryId;
    public InvalidParentCategoryId(String message, Long parentCategoryId) {
        super(message);
        this.message = message;
        this.parentCategoryId = parentCategoryId;
    }
}
