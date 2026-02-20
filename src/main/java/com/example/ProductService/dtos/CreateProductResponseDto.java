package com.example.ProductService.dtos;

import lombok.Data;

@Data
public class CreateProductResponseDto {
    private FakeStoreProductDto product;
    private ResponseStatus status;
}
