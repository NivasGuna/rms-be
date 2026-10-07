package com.arigs.rms.exception;

import com.arigs.rms.common.ApiError;
import com.arigs.rms.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts framework and business exceptions into consistent API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toApiError)
                .toList();
        return failure(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ApiError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        return failure(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return failure(HttpStatus.NOT_FOUND, ex.getMessage(), List.of(new ApiError(null, ex.getMessage())));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        return failure(HttpStatus.CONFLICT, ex.getMessage(), List.of(new ApiError(null, ex.getMessage())));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return failure(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), List.of(new ApiError(null, ex.getMessage())));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(RuntimeException ex) {
        return failure(HttpStatus.UNAUTHORIZED, "Authentication failed", List.of(new ApiError(null, ex.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return failure(HttpStatus.FORBIDDEN, "Access denied", List.of(new ApiError(null, "Insufficient permissions")));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDatabase(DataAccessException ex) {
        log.error("Database operation failed", ex);
        return failure(HttpStatus.INTERNAL_SERVER_ERROR, "Database operation failed", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unexpected API failure", ex);
        return failure(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", List.of());
    }

    private ApiError toApiError(FieldError fieldError) {
        return new ApiError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private ResponseEntity<ApiResponse<Void>> failure(HttpStatus status, String message, List<ApiError> errors) {
        return ResponseEntity.status(status).body(ApiResponse.failure(status.value(), message, errors));
    }
}
