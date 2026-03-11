# Product Service

A **Spring Boot microservice** responsible for managing **Products** and **Categories** in an e-commerce system.

This service exposes REST APIs to:

* Create products and categories
* Update product/category details
* Delete products and categories
* Retrieve products with pagination and sorting
* Search products using filters

This service is designed as part of a **scalable microservices-based e-commerce backend**.

---

# Features

* Product CRUD operations
* Category CRUD operations
* Product search with filters
* Pagination and sorting
* Global exception handling
* Logging using SLF4J
* DTO-based API layer
* Unit testing with JUnit and Mockito
* API documentation using Swagger / OpenAPI

---

# Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* MySQL / H2
* Lombok
* Maven
* Swagger (OpenAPI)
* JUnit
* Mockito

---

# Project Structure

```
src/main/java/com/example/productservice

├── controller
├── services
├── repositories
├── models
├── dtos
├── mappers
├── exceptions
├── controllerAdvices
└── config

src/test/java

└── services
```

---

# API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

This provides an interactive interface to test all APIs.

---

# API Endpoints

## Product APIs

### Create Product

POST /products

Request Body

```json
{
  "title": "iPhone 15",
  "price": 1200,
  "description": "Latest Apple smartphone",
  "categoryName": "Electronics",
  "imageUrl": "image-url"
}
```

---

### Get Product By Id

```
GET /products/{id}
```

Example

```
GET /products/1
```

---

### Update Product

```
PUT /products/{id}
```

---

### Delete Product

```
DELETE /products/{id}
```

---

### Get All Products

Supports pagination and sorting.

```
GET /products?page=0&size=10&sortBy=id&sortDirection=asc
```

---

### Search Products

Search products using filters.

```
GET /products/search
```

Example

```
GET /products/search?keyword=iphone&categoryName=Electronics&minPrice=500&maxPrice=2000&page=0&size=10
```

---

# Category APIs

### Create Category

```
POST /categories
```

Request Body

```json
{
  "name": "Electronics",
  "description": "Electronic items"
}
```

---

### Get Category By Id

```
GET /categories/{id}
```

---

### Get Category By Name

```
GET /categories/name/{name}
```

---

### Update Category

```
PUT /categories/{id}
```

---

### Delete Category

```
DELETE /categories/{id}
```

---

### Get All Categories

Supports pagination and sorting.

```
GET /categories?page=0&size=10&sortBy=id&sortDirection=asc
```

---

# Exception Handling

Global exception handling is implemented using `@ControllerAdvice`.

Example error response:

```json
{
  "timestamp": "2026-03-10T10:30:00",
  "status": 404,
  "error": "Product Not Found",
  "message": "Product with id 10 not found"
}
```

---

# Testing

Unit tests are implemented for the service layer using:

* JUnit
* Mockito

Test classes:

* `CategoryServiceImplTest`
* `ProductServiceImplTest`

Run tests using:

```
mvn test
```

---

# Logging

Logging is implemented using **SLF4J**.

Log levels used:

* INFO → create/update operations
* DEBUG → fetch/search operations
* WARN → invalid input or missing resources
* ERROR → unexpected system failures

---

# How to Run

Clone the repository

```
git clone <repo-url>
```

Navigate to the project

```
cd product-service
```

Build the project

```
mvn clean install
```

Run the application

```
mvn spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---

# Future Improvements

* Integration testing
* Docker containerization
* API Gateway integration
* Service discovery (Eureka)
* Distributed tracing
* Authentication and authorization using JWT

---

# Author

Backend microservice built using **Spring Boot** demonstrating:

* REST API design
* Pagination and filtering
* DTO mapping
* Global exception handling
* Logging
* Unit testing
* API documentation
