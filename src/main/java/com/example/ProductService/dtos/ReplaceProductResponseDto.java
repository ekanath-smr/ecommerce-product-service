package com.example.ProductService.dtos;

import lombok.Data;

@Data
public class ReplaceProductResponseDto {
    private FakeStoreProductDto product;
    private ResponseStatus status;
}
