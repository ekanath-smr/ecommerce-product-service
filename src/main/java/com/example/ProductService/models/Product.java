package com.example.ProductService.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseModel {
    private String title;
    private float price;
    private String description;
    private Category category;
    private String imageUrl;
}
