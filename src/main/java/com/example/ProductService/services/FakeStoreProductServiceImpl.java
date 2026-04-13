package com.example.ProductService.services;

import com.example.ProductService.dtos.ProductDto;
import com.example.ProductService.dtos.ProductRequestDto;
import com.example.ProductService.dtos.ProductSearchCriteria;
import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductNotFoundException;
import com.example.ProductService.mappers.ProductMapper;
import com.example.ProductService.models.Product;
import lombok.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.ProductService.mappers.ProductMapper.*;

@Service("fakeStoreProductServiceImpl")
//@Primary
public class FakeStoreProductServiceImpl implements ProductService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://fakestoreapi.com/products";

    public FakeStoreProductServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long productId) {
        // make an http call to fakestore api to get the product of the given productId and return the product.
        // https://fakestoreapi.com/products/{productId}
        // one of the way to make http call is to use RestTemplate class provided by Spring framework.
        // create a bean of RestTemplate class in the configuration class and inject it here using constructor injection.
        // RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/" + productId;
        ResponseEntity<ProductDto> responseEntity = restTemplate.getForEntity(url, ProductDto.class);
        if(!responseEntity.hasBody()) {
            throw new ProductNotFoundException("Invalid product id: " + productId, productId);
        }
        ProductDto productDto = responseEntity.getBody();
        return ProductMapper.mapProductDtoToProduct(productDto);
    }

    @Override
    public Page<Product> getAllProducts(int page, int size, String sortBy, String sortDirection) {
        return convertToPage(fetchAllProductsFromProvider(), page, size, sortBy, sortDirection);
    }

    @Override
    public Page<Product> searchProducts(ProductSearchCriteria criteria) {

        normalizeCriteria(criteria);

        List<Product> filteredProducts = fetchAllProductsFromProvider()
                .stream()
                .filter(product ->
                        criteria.getKeyword() == null ||
                                product.getTitle().toLowerCase()
                                        .contains(criteria.getKeyword().toLowerCase())
                )
                .filter(product ->
                        criteria.getCategoryName() == null ||
                                product.getCategory().getName()
                                        .equalsIgnoreCase(criteria.getCategoryName())
                )
                .filter(product ->
                        criteria.getMinPrice() == null ||
                                product.getPrice().compareTo(criteria.getMinPrice()) >= 0
                )
                .filter(product ->
                        criteria.getMaxPrice() == null ||
                                product.getPrice().compareTo(criteria.getMaxPrice()) <= 0
                )
                .toList();

        return convertToPage(
                filteredProducts, criteria.getPage(),
                criteria.getSize(), criteria.getSortBy(),
                criteria.getSortDirection()
        );
    }

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {
//        try {
//            getProductById(id);
//            throw new ProductAlreadyExistException("Product with id " + id + " already exists", id);
//        } catch (InvalidProductIdException e) {
            ProductDto productDto = getProductDtoFrom(0L, productRequestDto.getTitle(), productRequestDto.getPrice(),
                    productRequestDto.getDescription(), productRequestDto.getCategory(), productRequestDto.getImage());
            ResponseEntity<ProductDto> responseEntity = restTemplate.postForEntity(BASE_URL, productDto, ProductDto.class);
            return ProductMapper.mapProductDtoToProduct(responseEntity.getBody());
//        }
    }

    @Override
    public Product updateProduct(Long productId, ProductRequestDto productRequestDto) {
        try {
            getProductById(productId);
            String url = BASE_URL + "/" + productId;
            ProductDto productDto = getProductDtoFrom(
                    productId, productRequestDto.getTitle(), productRequestDto.getPrice(),
                    productRequestDto.getDescription(), productRequestDto.getCategory(), productRequestDto.getImage());
            restTemplate.put(url, productDto, ProductDto.class);
            return ProductMapper.mapProductDtoToProduct(productDto);
//            return getProductById(productId);
        } catch (ProductNotFoundException e) {
            throw new InvalidProductIdException("Product with productId " + productId + " does not exist", e.getProductId());
        }
    }

    @Override
    public void deleteProductById(Long productId) {
        try {
            getProductById(productId);
            String url = BASE_URL + "/" + productId;
            restTemplate.delete(url);
        } catch (ProductNotFoundException e) {
            throw new InvalidProductIdException("Product with id " + productId + " does not exist", e.getProductId());
        }
    }

    private List<Product> fetchAllProductsFromProvider() {
//        // raw type, not recommended
//        List products = restTemplate.getForEntity(BASE_URL, List.class).getBody();
//        return products;
//        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductDto>>() {});
        // Or Else map the response to an Array of FakeStoreProductDto (since there is no typeErasure in Arrays) and the convert it into a List of FakeStoreProductDto.
        ResponseEntity<ProductDto[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, ProductDto[].class);
        if(!response.hasBody() || response.getBody() == null || response.hasBody() && response.getBody() != null && response.getBody().length == 0) {
            return Collections.emptyList();
        }
        ProductDto[] responseArray = response.getBody();
        List<ProductDto> responseList = mapProductDtoArrayToProductDtoList(responseArray);
        return mapProductDtoListToProductList(responseList);
    }

    private Page<Product> convertToPage(List<Product> productList, int pageNumber, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Comparator<Product> comparator = getProductComparator(sortBy, direction);
//        productList.sort(comparator);
        List<Product> sortedList = productList.stream()
                .sorted(comparator)
                .toList();
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(direction, sortBy));
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sortedList.size());

        List<Product> pagedList = start >= sortedList.size() ? List.of() : sortedList.subList(start, end);
        return new PageImpl<>(pagedList, pageable, sortedList.size());
    }

    private static @NonNull Comparator<Product> getProductComparator(String sortBy, Sort.Direction direction) {
        Comparator<Product> comparator = switch (sortBy) {
            case "title" -> Comparator.comparing(Product::getTitle);
            case "price" -> Comparator.comparing(Product::getPrice);
            case "createdAt" -> Comparator.comparing(Product::getCreatedAt);
            case "id" -> Comparator.comparing(Product::getId);
            case "category" -> Comparator.comparing(product -> product.getCategory().getName());
            default -> throw new IllegalArgumentException("Invalid sortBy field");
        };
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private void normalizeCriteria(ProductSearchCriteria criteria) {
        if (!StringUtils.hasText(criteria.getKeyword())) {
            criteria.setKeyword(null);
        }
        if (!StringUtils.hasText(criteria.getCategoryName())) {
            criteria.setCategoryName(null);
        }
    }

}