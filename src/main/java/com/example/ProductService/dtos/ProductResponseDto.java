package com.example.ProductService.dtos;

import lombok.Data;

@Data
public class ProductResponseDto {
    private ProductDto product;
    private ResponseStatus status;
}
