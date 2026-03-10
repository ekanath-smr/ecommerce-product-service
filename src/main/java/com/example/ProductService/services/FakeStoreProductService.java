package com.example.ProductService.services;

import com.example.ProductService.dtos.ProductDto;
import com.example.ProductService.exceptions.InvalidCategoryNameException;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.example.ProductService.mappers.ProductMapper.*;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long productId) throws InvalidProductIdException {
        // make an http call to fakestore api to get the product of the given productId and return the product.
        // https://fakestoreapi.com/products/{productId}
        // one of the way to make http call is to use RestTemplate class provided by Spring framework.
        // create a bean of RestTemplate class in the configuration class and inject it here using constructor injection.
        // RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products/" + productId;
        // FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(url, FakeStoreProductDto.class);
        ResponseEntity<ProductDto> responseEntity = restTemplate.getForEntity(url, ProductDto.class);
        ProductDto productDto = responseEntity.getBody();
        if(productDto == null) {
            throw new InvalidProductIdException("Invalid product id: " + productId, productId);
        }
        return ProductMapper.mapProductDtoToProduct(productDto);
    }

    @Override
    public Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public Page<Product> searchProducts(String keyword, String categoryName, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDirection) {
        return null;
    }

//    @Override
//    public List<Product> getAllProducts() {
//        String url = "https://fakestoreapi.com/products";
////        List products = restTemplate.getForEntity(url, List.class).getBody();
////        return products;
//        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDto>>() {});
////        // Or Else map the response to an Array of FakeStoreProductDto (since there is no typeErasure in Arrays) and the convert it into a List of FakeStoreProductDto.
////        ResponseEntity<ProductDto[]> response = restTemplate.exchange(url, HttpMethod.GET, null, ProductDto[].class);
////        ProductDto[] responseArray = response.getBody();
////        // use this responseList in the place of response.getBody();
////        List<ProductDto> responseList = mapProductDtoArrayToProductDtoList(responseArray);
//        if(response.getBody() == null) {
//            return Collections.emptyList();
//        }
//        return mapProductDtoListToProductList(response.getBody());
//    }

    @Override
    public Product createProduct(String title, double price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException, InvalidCategoryNameException {
//        try {
//            getProductById(id);
//            throw new ProductAlreadyExistException("Product with id " + id + " already exists", id);
//        } catch (InvalidProductIdException e) {
            String url = "https://fakestoreapi.com/products";
            ProductDto productDto = getProductDto(0L, title, price, description, categoryName, imageUrl);
            ResponseEntity<ProductDto> responseEntity = restTemplate.postForEntity(url, productDto, ProductDto.class);
            return ProductMapper.mapProductDtoToProduct(responseEntity.getBody());
//        }
    }

    @Override
    public Product updateProduct(Long productId, String title, double price, String description, String categoryName, String imageUrl) throws InvalidProductIdException {
        try {
            getProductById(productId);
            String url = "https://fakestoreapi.com/products/" + productId;
            ProductDto productDto = getProductDto(productId, title, price, description, categoryName, imageUrl);
            restTemplate.put(url, productDto, ProductDto.class);
            return getProductById(productId);
        } catch (InvalidProductIdException e) {
            throw new InvalidProductIdException("Product with productId " + productId + " does not exist", e.getProductId());
        }
    }

    @Override
    public void deleteProductById(Long productId) throws InvalidProductIdException {
        try {
            getProductById(productId);
            String url = "https://fakestoreapi.com/products/" + productId;
            restTemplate.delete(url);
        } catch (InvalidProductIdException e) {
            throw new InvalidProductIdException("Product with id " + productId + " does not exist", e.getProductId());
        }
    }

//    @Override
//    public List<Product> searchProducts(String keyword) {
//        return List.of();
//    }

}