package com.example.ProductService.controllerAdvices;

import com.example.ProductService.dtos.ErrorResponseDto;
import com.example.ProductService.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected runtime exception occurred: {}", ex.getMessage(), ex);
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server side error (RuntimeException)", "There is some RuntimeException in the server side");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
        logger.warn("Invalid product id received: {}", invalidProductIdException.getProductId());
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "Invalid product id.", "Product with id: " + invalidProductIdException.getProductId() + ", not found in database.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleProductAlreadyExistException(ProductAlreadyExistException productAlreadyExistException) {
        logger.warn("Product already exists with title: {}", productAlreadyExistException.getTitle());
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Collision, Cannot create product.", "Product with title: '" + productAlreadyExistException.getTitle() + "', already exist in the database.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCategoryNameException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCategoryNameException(InvalidCategoryNameException invalidCategoryNameException) {
        logger.warn("Invalid category name: {}", invalidCategoryNameException.getCategoryName());
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Invalid category name.", "Category with name: '" + invalidCategoryNameException.getCategoryName() + "', is not a valid registered category name");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException categoryAlreadyExistsException) {
        logger.warn("Category already exists with name: {}", categoryAlreadyExistsException.getName());
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
                "Category already exist.", "Category with name '" + categoryAlreadyExistsException.getName() + "', already exists.");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCategoryIdException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCategoryIdException(InvalidCategoryIdException invalidCategoryIdException) {
        logger.warn("Invalid category id requested");
        ErrorResponseDto error = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                "Invalid category Id", "Category with id: " + invalidCategoryIdException.getCategoryId() + ", is not a valid registered category id.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
