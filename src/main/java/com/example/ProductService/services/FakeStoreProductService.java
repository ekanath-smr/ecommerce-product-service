package com.example.ProductService.services;

import com.example.ProductService.dtos.FakeStoreProductDto;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import com.example.ProductService.mappers.FakeStoreProductMapper;
import com.example.ProductService.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FakeStoreProductService implements ProductService {

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws InvalidProductIdException {
        // make a http call to fakestore api to get the product of the given productId and return the product.
        // https://fakestoreapi.com/products/{productId}
        // one of the way to make http call is to use RestTemplate class provided by Spring framework.
        // create a bean of RestTemplate class in the configuration class and inject it here using constructor injection.
        // RestTemplate restTemplate = new RestTemplate();
        String url = "https://fakestoreapi.com/products/" + productId;
        // FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(url, FakeStoreProductDto.class);
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.getForEntity(url, FakeStoreProductDto.class);
        FakeStoreProductDto fakeStoreProductDto = responseEntity.getBody();
        if(fakeStoreProductDto == null) {
            throw new InvalidProductIdException("Invalid product id: " + productId);
        }
        return FakeStoreProductMapper.mapFakeStoreProductDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProduct() {
        String url = "https://fakestoreapi.com/products";
        List products = restTemplate.getForEntity(url, List.class).getBody();
        return products;
    }

    @Override
    public Product createProduct(Long id, String title, float price, String description, String categoryName, String imageUrl) throws ProductAlreadyExistException {
        try {
            getSingleProduct(id);
            throw new ProductAlreadyExistException("Product with id " + id + " already exists");
        } catch (InvalidProductIdException e) {
            String url = "https://fakestoreapi.com/products";
            FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
            fakeStoreProductDto.setId(id);
            fakeStoreProductDto.setTitle(title);
            fakeStoreProductDto.setPrice(price);
            fakeStoreProductDto.setDescription(description);
            fakeStoreProductDto.setCategory(categoryName);
            fakeStoreProductDto.setImage(imageUrl);
            ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.postForEntity(url, fakeStoreProductDto, FakeStoreProductDto.class);
            return FakeStoreProductMapper.mapFakeStoreProductDtoToProduct(responseEntity.getBody());
        }
    }

    @Override
    public Product replaceProduct(Long id, String title, float price, String description, String categoryName, String imageUrl) throws InvalidProductIdException {
        try {
            getSingleProduct(id);
            String url = "https://fakestoreapi.com/products/" + id;
            FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
            fakeStoreProductDto.setId(id);
            fakeStoreProductDto.setTitle(title);
            fakeStoreProductDto.setPrice(price);
            fakeStoreProductDto.setDescription(description);
            fakeStoreProductDto.setCategory(categoryName);
            fakeStoreProductDto.setImage(imageUrl);
            restTemplate.put(url, fakeStoreProductDto, FakeStoreProductDto.class);
            return getSingleProduct(id);
        } catch (InvalidProductIdException e) {
            throw new InvalidProductIdException("Product with id " + id + " does not exist");
        }
    }

    @Override
    public void deleteProduct(Long productId) throws InvalidProductIdException {
        try {
            getSingleProduct(productId);
            String url = "https://fakestoreapi.com/products/" + productId;
            restTemplate.delete(url);
        } catch (InvalidProductIdException e) {
            throw new InvalidProductIdException("Product with id " + productId + " does not exist");
        }
    }

}
