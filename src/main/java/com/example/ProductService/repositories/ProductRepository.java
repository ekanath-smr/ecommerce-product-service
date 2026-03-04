package com.example.ProductService.repositories;

import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.projections.ProductWithTitleAndPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //Derived Queries // select * from products p where p.id = ?
    // Optional is used for compile time safety check for null pointer exception NPE
    @Override
    Optional<Product> findById(Long aLong);

    // Derived Queries // select * from products
    @Override
    List<Product> findAll();

    // Derived Queries // select * from products where title = ?
    List<Product> findByTitle(String title);

    // Derived Queries //select * from products where title LIKE '%iPhone%'
    Page<Product> findByTitleContainsIgnoreCase(String str, Pageable pageable);

    // Derived Queries // select * from products where price >= start and price <= end
    List<Product> findByPriceBetween(Double start, Double end);

    // Derived Queries
    //select * from products where title LIKE '%str%' and price >= start and price <= end
    List<Product> findByTitleContainsIgnoreCaseAndPriceBetween(String title, Double start, Double end);

    // Derived Queries
    List<Product> findByCreatedAtBetween(Date start, Date end);

    // Built-in CRUD methods from JpaRepository
    @Override
    void deleteById(Long aLong);

    // Built-in CRUD methods from JpaRepository
    Product save(Product product);

    // Native Query.
    // Query : Find the title and price of the product with id = 10;
    // select title, price from products where id = 10;
    // here we use the exact created table name and column name in the query.
    // Not portable from one database to another.
//    @Query(value = "select p.title, p.price from products p where p.id = :id", nativeQuery = true)
//    List<ProductWithTitleAndPrice> findTitleAndPriceById(@Param("id") Long id);

    // HQL Query.
    // Query : Find the title and price of the product with id = 10;
    // select title, price from products where id = 10;
    // here we use entity className and entity fieldName directly in the query.
    // Portable from one database to another.
    @Query("select p.title, p.price from Product p where p.id = :id")
    ProductWithTitleAndPrice findTitleAndPriceById(@Param("id") Long id);

    // Derived Queries
    Optional<Product> findByCategory(Category category);

    // Derived Queries
    Optional<Product> findByCategory_Name(String categoryName);

}
