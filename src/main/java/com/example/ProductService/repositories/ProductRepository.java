package com.example.ProductService.repositories;

import com.example.ProductService.models.Category;
import com.example.ProductService.models.Product;
import com.example.ProductService.projections.ProductWithTitleAndPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    //Derived Queries // select * from products p where p.id = ?
    // Optional is used for compile time safety check for null pointer exception NPE
    @Override
    Optional<Product> findById(Long aLong);

    // Derived Queries // select * from products
    @Override
    List<Product> findAll();

    // Derived Queries // select * from products where title = ?
    Optional<Product> findByTitle(String title);

    // Derived Queries // select count(*) >= 1 from products where category_id = ?
    boolean existsByCategory_Id(Long categoryId);

    // Derived Queries //select * from products where title LIKE '%iPhone%'
//    Page<Product> findByTitleContainsIgnoreCase(String keyword, Pageable pageable);

    // Derived Queries // select * from products where price >= start and price <= end
//    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Derived Queries
    //select * from products where title LIKE '%str%' and price >= start and price <= end
//    List<Product> findByTitleContainsIgnoreCaseAndPriceBetween(String keyword, Double minPrice, Double maxPrice);

    // Derived Queries
//    List<Product> findByCreatedAtBetween(Date start, Date end);

    // Built-in CRUD methods from JpaRepository
    @Override
    void deleteById(Long productId);

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
    Optional<ProductWithTitleAndPrice> findTitleAndPriceById(@Param("id") Long id);

    // Derived Queries
//    Optional<Product> findByCategory(Category category);

    // Derived Queries
//    Optional<Product> findByCategory_Name(String categoryName);

    // This way of searching is not reliable because filters can grow, so use jpa specifications
    @Query("""
            select p
            from Product p
            where ((:keyword is null or lower(p.title) like lower(concat('%', :keyword, '%')))
            and (:categoryName is null or p.category.name = :categoryName)
            and (:minPrice is null or p.price >= :minPrice)
            and (:maxPrice is null or p.price <= :maxPrice))
            """)
    Page<Product> search(@Param("keyword") String keyword, @Param("categoryName") String categoryName,
                         @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

}
