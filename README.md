# Ecommerce Product Service

A production-style **Spring Boot microservice** for managing **Products** and **Hierarchical Categories** in an e-commerce platform.

Designed as part of a scalable **microservices-based backend architecture**, this service provides robust REST APIs for product/catalog management with validation, pagination, filtering, exception handling, and business-rule enforcement.

---

# Features

## Product Management
- Create Product
- Update Product
- Delete Product
- Get Product By ID
- Get All Products with Pagination & Sorting
- Search Products with Dynamic Filters

## Category Management
- Create Category
- Update Category
- Delete Category
- Get Category By ID / Name
- Hierarchical Parent-Child Category Support
- Circular Hierarchy Prevention
- Prevent Deletion of Parent Categories with Children

## Backend Engineering Features
- DTO-based Request/Response Architecture
- Centralized Global Exception Handling
- Validation using Jakarta Bean Validation
- SLF4J Structured Logging
- Swagger/OpenAPI Documentation
- Unit Testing with JUnit + Mockito

---

# Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **MySQL / H2**
- **Lombok**
- **Maven**
- **Swagger / OpenAPI**
- **JUnit 5**
- **Mockito**

---

# Architecture / Design Highlights

- Implemented **Layered Architecture**
    - Controller Layer
    - Service Layer
    - Repository Layer

- Applied **DTO + Mapper Pattern** to decouple API contract from persistence models

- Built **Hierarchical Category Management**
    - Parent-child relationships
    - Circular dependency prevention
    - Business-rule enforced deletion constraints

- Implemented **Dynamic Product Search**
    - Keyword Search
    - Category Filter
    - Price Range Filter
    - Pagination & Sorting

---

# Project Structure

```text
src/main/java/com/example/ProductService
├── controllers
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

Swagger UI:

http://localhost:8080/swagger-ui/index.html

Interactive API documentation available for all endpoints.

---

# API Endpoints

## Product APIs

### Create Product
`POST /products`

Request Body:

```json
{
  "title": "iPhone 15",
  "price": 1200,
  "description": "Latest Apple smartphone",
  "categoryName": "Electronics",
  "imageUrl": "image-url"
}
```

### Get Product By ID
`GET /products/{id}`

### Update Product
`PATCH /products/{id}`

### Delete Product
`DELETE /products/{id}`

### Get All Products
`GET /products?page=0&size=10&sortBy=id&sortDirection=asc`

### Search Products
`GET /products/search`

Example:

`GET /products/search?keyword=iphone&categoryName=Electronics&minPrice=500&maxPrice=2000&page=0&size=10`

---

## Category APIs

### Create Category
`POST /categories`

Request Body:

```json
{
  "name": "Mobiles",
  "description": "Mobile Phones",
  "parentCategoryName": "Electronics"
}
```

### Get Category By ID
`GET /categories/{id}`

### Get Category By Name
`GET /categories?name=Electronics`

### Update Category
`PATCH /categories/{id}`

### Delete Category
`DELETE /categories/{id}`

### Get All Categories
`GET /categories?page=0&size=10&sortBy=id&sortDirection=asc`

---

# Exception Handling

Centralized exception handling via `@ControllerAdvice`.

Handles:

- Invalid Product / Category IDs
- Duplicate Product / Category Creation
- Invalid Parent Category Assignment
- Circular Category Hierarchy
- Parent Category Deletion Restriction
- Validation Errors
- Unexpected Runtime Exceptions

Example Error Response:

```json
{
  "timestamp": "2026-04-13T10:30:00",
  "status": 404,
  "error": "Invalid Product Id",
  "message": "Product with id 10 not found"
}
```

---

# Testing

Unit Tests implemented for Service Layer using:

- **JUnit 5**
- **Mockito**

Test Coverage Includes:

- ProductServiceImplTest
- CategoryServiceImplTest

Run Tests:

```bash
mvn test
```

---

# Logging Strategy

Implemented structured logging using **SLF4J**.

| Level | Usage |
|--------|-------|
| INFO | Create / Update Operations |
| DEBUG | Fetch / Search Operations |
| WARN | Invalid Inputs / Missing Resources |
| ERROR | Unexpected Runtime Failures |

---

# How to Run

Clone Repository:

```bash
git clone https://github.com/ekanath-smr/ecommerce-product-service.git
```

Navigate:

```bash
cd ecommerce-product-service
```

Build:

```bash
mvn clean install
```

Run:

```bash
mvn spring-boot:run
```

Application Starts At:

`http://localhost:8080`

Swagger:

`http://localhost:8080/swagger-ui/index.html`

---

# Future Enhancements

- Integration Testing
- Docker Containerization
- API Gateway Integration
- Eureka Service Discovery
- Distributed Tracing
- JWT Authentication / Authorization
- Redis Caching
- Elasticsearch for Advanced Search

---

# Resume-Worthy Highlights

This project demonstrates proficiency in:

- REST API Design
- Backend Architecture
- Spring Boot Best Practices
- JPA/Hibernate Modeling
- Pagination / Filtering / Sorting
- Exception Handling
- Unit Testing
- Microservice-Oriented Design
- Production-Style Code Organization

---

# Author

**Ekanath S M R**

Backend Engineer | Java + Spring Boot Developer

GitHub:  
https://github.com/ekanath-smr