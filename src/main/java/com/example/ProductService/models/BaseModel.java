package com.example.ProductService.models;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
}
