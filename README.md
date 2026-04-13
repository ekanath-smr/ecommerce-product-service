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

- Implemented **layered architecture**:
    - Controller Layer
    - Service Layer
    - Repository Layer

- Applied **DTO + Mapper Pattern** to decouple API contract from persistence models.

- Built **Hierarchical Category Management**:
    - Parent-child relationships
    - Circular dependency prevention
    - Business-rule enforced deletion constraints

- Implemented **Dynamic Product Search**:
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