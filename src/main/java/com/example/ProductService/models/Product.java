package com.example.ProductService.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private Double price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private String imageUrl;
}
