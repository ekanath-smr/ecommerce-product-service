package com.example.ProductService.dtos;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private CategoryDto category;
    private ResponseStatus status;
}
