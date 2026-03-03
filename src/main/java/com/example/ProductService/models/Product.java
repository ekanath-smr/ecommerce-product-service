package com.example.ProductService.models;

import jakarta.persistence.*;
import lombok.*;

//@Data
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
public class Product extends BaseModel {
    private String title;
    private float price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String imageUrl;
}
