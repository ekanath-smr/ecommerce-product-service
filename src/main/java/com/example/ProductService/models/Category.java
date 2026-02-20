package com.example.ProductService.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseModel {
    private String name;
}
