# Ecommerce Product Service

A production-grade **Spring Boot microservice** responsible for managing Products and Hierarchical Categories within a scalable e-commerce platform.

The service provides catalog management APIs, advanced product search capabilities, Redis-based caching, service discovery integration, and seamless communication within a distributed microservices architecture.

---

# Features

## Product Management

* Create Product
* Update Product
* Delete Product
* Get Product By ID
* Validate Product Existence
* Get All Products with Pagination & Sorting
* Dynamic Product Search

## Category Management

* Create Category
* Update Category
* Delete Category
* Get Category By ID
* Get Category By Name
* Parent-Child Category Hierarchy
* Circular Hierarchy Prevention
* Parent Category Deletion Protection

## Performance & Scalability

* Redis Caching for Product Retrieval
* Reduced Database Load
* Faster Read Operations

## Microservice Integration

* Eureka Service Discovery Client
* API Gateway Routing Support
* Feign Client Compatibility
* Inter-Service Communication Ready

## Engineering Features

* DTO-Based API Layer
* Mapper Pattern
* Global Exception Handling
* Structured Logging using SLF4J
* Validation using Jakarta Bean Validation
* Swagger/OpenAPI Documentation
* Unit Testing using JUnit & Mockito

---

# Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* MySQL
* Redis
* Spring Cache
* Eureka Discovery Client
* OpenFeign
* Lombok
* Maven
* Swagger / OpenAPI
* JUnit 5
* Mockito

---

# Architecture Highlights

## Layered Architecture

* Controller Layer
* Service Layer
* Repository Layer

## DTO + Mapper Pattern

Decouples API contracts from persistence entities and improves maintainability.

## Hierarchical Category Design

Supports:

* Parent Categories
* Child Categories
* Multi-level Category Trees
* Circular Dependency Validation

## Dynamic Product Search

Supports:

* Keyword Search
* Category Filter
* Price Range Filter
* Pagination
* Sorting

## Redis Caching

Product retrieval APIs are cached to reduce repeated database lookups and improve response time.

## Service Discovery

Registered as a Eureka Client for dynamic service registration and discovery.

## API Gateway Ready

Designed to be consumed through a centralized API Gateway with load balancing and routing.

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
├── specifications
├── exceptions
├── controllerAdvices
├── config
└── cache

src/test/java

└── services
```

---

# API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html

Provides interactive documentation and API testing support.

---

# Product APIs

## Create Product

POST /products

```json
{
  "title": "iPhone 15",
  "price": 1200,
  "description": "Latest Apple smartphone",
  "categoryName": "Electronics",
  "imageUrl": "image-url"
}
```

## Get Product By ID

GET /products/{id}

## Validate Product Existence

GET /products/{id}/exists

Response:

```json
true
```

or

```json
false
```

## Update Product

PATCH /products/{id}

## Delete Product

DELETE /products/{id}

## Get All Products

GET /products?page=0&size=10&sortBy=id&sortDirection=asc

## Search Products

GET /products/search

Example:

GET /products/search?keyword=iphone&categoryName=Electronics&minPrice=500&maxPrice=2000&page=0&size=10

---

# Category APIs

## Create Category

POST /categories

```json
{
  "name": "Mobiles",
  "description": "Mobile Phones",
  "parentCategoryName": "Electronics"
}
```

## Get Category By ID

GET /categories/{id}

## Get Category By Name

GET /categories?name=Electronics

## Update Category

PATCH /categories/{id}

## Delete Category

DELETE /categories/{id}

## Get All Categories

GET /categories?page=0&size=10&sortBy=id&sortDirection=asc

---

# Exception Handling

Centralized exception handling implemented using @ControllerAdvice.

Handled Scenarios:

* Product Not Found
* Invalid Product Id
* Duplicate Product Creation
* Invalid Category Name
* Duplicate Category Creation
* Invalid Parent Category
* Circular Category Hierarchy
* Parent Category Deletion Restriction
* Validation Errors
* Unexpected Runtime Exceptions

Example Response:

```json
{
  "timestamp": "2026-04-13T10:30:00",
  "status": 404,
  "error": "Product Not Found",
  "message": "Product with id 10 not found"
}
```

---

# Redis Caching

Caching is implemented for frequently accessed product APIs.

Benefits:

* Reduced Database Queries
* Lower Response Latency
* Improved Scalability
* Better Throughput Under Load

---

# Testing

Unit tests implemented using:

* JUnit 5
* Mockito

Coverage Includes:

* ProductServiceImplTest
* CategoryServiceImplTest

Run Tests:

```bash
mvn test
```

---

# Logging Strategy

Structured logging implemented using SLF4J.

| Level | Purpose                              |
| ----- | ------------------------------------ |
| INFO  | Create / Update / Delete Operations  |
| DEBUG | Fetch / Search Operations            |
| WARN  | Invalid Requests / Missing Resources |
| ERROR | Unexpected Failures                  |

---

# Service Registration

This service registers itself with Eureka Service Discovery.

Example Registration:

```text
PRODUCT-SERVICE
```

Enables:

* Dynamic Service Discovery
* Client-side Load Balancing
* API Gateway Routing

---

# How to Run

Clone Repository

```bash
git clone https://github.com/ekanath-smr/ecommerce-product-service.git
```

Navigate

```bash
cd ecommerce-product-service
```

Build

```bash
mvn clean install
```

Run

```bash
mvn spring-boot:run
```

Application:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Future Enhancements

* Kafka Event Publishing
* Distributed Tracing
* Elasticsearch Integration
* Integration Testing
* Docker Containerization
* Kubernetes Deployment
* Observability with Prometheus & Grafana

---

# Resume Highlights

This project demonstrates:

* Microservices Architecture
* Spring Boot Best Practices
* REST API Design
* Service Discovery
* API Gateway Integration
* Redis Caching
* DTO & Mapper Pattern
* Dynamic Search using Specifications
* Hierarchical Data Modeling
* Exception Handling
* Logging & Observability
* Unit Testing
* Production-Ready Backend Development

---

# Author

**Ekanath S M R**

Java Backend Engineer | Spring Boot | Microservices | Distributed Systems

GitHub:
https://github.com/ekanath-smr
