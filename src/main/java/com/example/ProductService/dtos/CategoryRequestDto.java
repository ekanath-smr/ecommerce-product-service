package com.example.ProductService.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    @NotBlank(message = "Category name is required")
    private String name;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    private Long parentCategoryId;
    private String parentCategoryName;
}
