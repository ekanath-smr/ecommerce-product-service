package com.example.ProductService.controllerAdvices;

import com.example.ProductService.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected runtime exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("GlobalExceptionHandler: There is some RuntimeException in the Server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
        logger.warn("Invalid product id received: {}", invalidProductIdException.getProductId());
        return new ResponseEntity<>("GlobalExceptionHandler: " + invalidProductIdException.getProductId() + " is an invalid product id, Please pass a valid product id", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<String> handleProductAlreadyExistException(ProductAlreadyExistException productAlreadyExistException) {
        logger.warn("Product already exists with id: {}", productAlreadyExistException.getProductId());
        return new ResponseEntity<>("GlobalExceptionHandler: Product with productId " + productAlreadyExistException.getProductId() + " already exist.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException categoryAlreadyExistsException) {
        logger.warn("Category already exists with name: {}", categoryAlreadyExistsException.getName());
        return new ResponseEntity<>("Category with name " + categoryAlreadyExistsException.getName() + ", already exists.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCategoryNameException.class)
    public ResponseEntity<String> handleInvalidCategoryNameException(InvalidCategoryNameException invalidCategoryNameException) {
        logger.warn("Invalid category name requested");
        return new ResponseEntity<>("Invalid category name", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCategoryIdException.class)
    public ResponseEntity<String> handleInvalidCategoryIdException(InvalidCategoryIdException invalidCategoryIdException) {
        logger.warn("Invalid category id requested");
        return new ResponseEntity<>("Invalid category Id", HttpStatus.NOT_FOUND);
    }

}
