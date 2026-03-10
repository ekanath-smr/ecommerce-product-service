package com.example.ProductService.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequestDto {
    @NotBlank
    private String title;
    @NotNull
    @Positive
    private double price;
    @Size(max = 1000)
    private String description;
    @NotBlank
    private String category;
    private String image;
}
