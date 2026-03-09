package com.example.ProductService.mappers;

import com.example.ProductService.dtos.ProductDto;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static Product mapProductDtoToProduct(ProductDto productDto) {
        if(productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImage());

        Category category = new Category();
        category.setName(productDto.getCategory());
        product.setCategory(category);

        return product;
    }

    public static ProductDto mapProductToProductDto(Product product) {
        if(product == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImage(product.getImageUrl());
        if(product.getCategory() != null) {
            productDto.setCategory(product.getCategory().getName());
        }
        return productDto;
    }

    public static List<ProductDto> mapProductListToProductDtoList(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products) {
            productDtos.add(mapProductToProductDto(product));
        }
        return productDtos;
    }

    public static List<Product> mapProductDtoListToProductList(List<ProductDto> productDtos) {
        List<Product> products = new ArrayList<>();
        for(ProductDto productDto : productDtos) {
            products.add(mapProductDtoToProduct(productDto));
        }
        return products;
    }

    public static ProductDto getProductDto(Long id, String title, double price, String description, String categoryName, String imageUrl) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setTitle(title);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategory(categoryName);
        productDto.setImage(imageUrl);
        return productDto;
    }

    public static List<ProductDto> mapProductDtoArrayToProductDtoList(ProductDto[] productDtos) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for(ProductDto productDto : productDtos) {
            productDtoList.add(productDto);
        }
        return productDtoList;
    }
}
