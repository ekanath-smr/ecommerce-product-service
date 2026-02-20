package com.example.ProductService.dtos;

import com.example.ProductService.models.Product;
import lombok.Data;

@Data
public class GetSingleProductResponseDto {
    private Product product;
    private ResponseStatus status;
}
