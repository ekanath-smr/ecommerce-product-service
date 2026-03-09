package com.example.ProductService.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    private String name;
    @Size(max = 500)
    private String description;
}
