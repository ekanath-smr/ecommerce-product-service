package com.example.ProductService.dtos;

import lombok.Data;

@Data
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
}
