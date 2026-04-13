package com.example.ProductService.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    @NotBlank
    private String title;
    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
    @Size(max = 1000)
    private String description;
    @NotBlank
    private String category;
    private String image;
}
