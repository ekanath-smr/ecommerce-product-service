package com.example.ProductService.controllerAdvices;

import com.example.ProductService.dtos.ErrorResponseDto;
import com.example.ProductService.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ================= VALIDATION EXCEPTIONS =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        logger.warn("Validation failed: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request Parameter", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage());
    }

    // ================= PRODUCT EXCEPTIONS =================

    @ExceptionHandler({InvalidProductIdException.class, ProductNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(RuntimeException ex) {
        logger.warn("Product not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Product Not Found", ex.getMessage());
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleProductAlreadyExistsException(ProductAlreadyExistException ex) {
        logger.warn("Product already exists: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Product Already Exists", ex.getMessage());
    }

    // ================= CATEGORY EXCEPTIONS =================

    @ExceptionHandler({
            InvalidCategoryIdException.class, InvalidCategoryNameException.class,
            InvalidParentCategoryId.class, InvalidParentCategoryName.class
    })
    public ResponseEntity<ErrorResponseDto> handleCategoryNotFoundExceptions(RuntimeException ex) {
        logger.warn("Category lookup failed: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Category Not Found", ex.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        logger.warn("Category already exists: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Category Already Exists", ex.getMessage());
    }

    @ExceptionHandler({InvalidCategoryHierarchyException.class, CannotDeleteCategoryException.class,
            InvalidParentAssignmentException.class, CannotDeleteParentCategoryException.class})
    public ResponseEntity<ErrorResponseDto> handleCategoryBusinessRuleViolations(RuntimeException ex) {
        logger.warn("Category business rule violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Category Operation Failed", ex.getMessage());
    }

    // ================= FALLBACK =================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected runtime exception occurred", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }

    // ================= HELPER =================

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String error, String message) {
        ErrorResponseDto dto = new ErrorResponseDto(
                LocalDateTime.now(), status.value(),
                error, message
        );
        return ResponseEntity.status(status).body(dto);
    }
}