package com.example.ProductService.mappers;

import com.example.ProductService.dtos.FakeStoreProductDto;
import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;

public class FakeStoreProductMapper {
    public static Product mapFakeStoreProductDtoToProduct(FakeStoreProductDto fakeStoreProductDto) {
//        if(fakeStoreProductDto == null) {
//            return null;
//        }
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImageUrl(fakeStoreProductDto.getImage());

        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);

        return product;
    }

    public static FakeStoreProductDto mapProductToFakeStoreProductDto(Product product) {
//        if(product == null) {
//            return null;
//        }
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
