package com.example.ProductService.controllerAdvices;

import com.example.ProductService.exceptions.InvalidProductIdException;
import com.example.ProductService.exceptions.ProductAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException() {
        return new ResponseEntity<>("GlobalExceptionHandler: There is some RuntimeException in the Server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleProductNotFoundException(InvalidProductIdException invalidProductIdException) {
        return new ResponseEntity<>("GlobalExceptionHandler: " + invalidProductIdException.getProductId() + " is an invalid product id, Please pass a valid product id", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<String> handleProductAlreadyExistException(ProductAlreadyExistException productAlreadyExistException) {
        return new ResponseEntity<>("GlobalExceptionHandler: Product with productId " + productAlreadyExistException.getProductId() + " already exist.", HttpStatus.BAD_REQUEST);
    }

}
