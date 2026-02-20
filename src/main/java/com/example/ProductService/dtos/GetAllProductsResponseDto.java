package com.example.ProductService.dtos;

import com.example.ProductService.models.Product;
import lombok.Data;

import java.util.List;

@Data
public class GetAllProductsResponseDto {
    private List<Product> products;
    private ResponseStatus status;
}
