package com.example.ProductService.mappers;

import com.example.ProductService.dtos.CategoryDto;
import com.example.ProductService.dtos.CategoryResponseDto;
import com.example.ProductService.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryDto mapCategoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        if(category.getParentCategory() != null) {
            categoryDto.setParentCategoryId(category.getParentCategory().getId());
            categoryDto.setParentCategoryName(category.getParentCategory().getName());
        }
        return categoryDto;
    }

    public static List<CategoryDto> mapCategoryListToCategoryDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Category category : categories) {
            categoryDtos.add(mapCategoryToCategoryDto(category));
        }
        return categoryDtos;
    }

}