# Product Service

A Spring Boot microservice that manages **Products** and **Categories** for an e-commerce system.  
This service provides REST APIs to create, update, delete, search, and retrieve products and categories.

---

## Features

- Product CRUD operations
- Category CRUD operations
- Product search with filters
- Pagination and sorting
- Global exception handling
- Logging using SLF4J
- DTO mapping
- Unit testing with JUnit and Mockito

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL / H2
- Lombok
- Maven
- JUnit
- Mockito

---

## Project Structure

src  
├── controller  
├── services  
├── repositories  
├── models  
├── dtos  
├── mappers  
├── exceptions  
├── controllerAdvices  
└── tests  

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

GET /products/{id}

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

Search products using multiple filters.

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

- JUnit
- Mockito

Test classes:

- CategoryServiceImplTest
- ProductServiceImplTest

Run tests:

```
mvn test
```

---

# Logging

Logging is implemented using **SLF4J**.

Log levels used:

- INFO → create/update operations
- DEBUG → fetch/search operations
- WARN → invalid input or missing resources

---

# How to Run

Clone the repository

```
git clone <repo-url>
```

Navigate to project

```
cd product-service
```

Run application

```
mvn spring-boot:run
```

Application starts at

```
http://localhost:8080
```

---

# Future Improvements

- API documentation using Swagger
- Integration tests
- Docker containerization
- API Gateway integration
- Service discovery

---

# Author

Backend service developed using Spring Boot demonstrating REST API design, pagination, filtering, exception handling, logging, and unit testing.
