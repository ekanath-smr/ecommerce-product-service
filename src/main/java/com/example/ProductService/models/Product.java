package com.example.ProductService.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_title", columnList = "title"),
                @Index(name = "idx_product_category", columnList = "category_id")
        }
)
public class Product extends BaseModel {
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String imageUrl;
}
