package com.example.ProductService.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductSearchCriteria {
    private String keyword;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sortBy = "price";
    @Builder.Default
    private String sortDirection = "asc";
}
