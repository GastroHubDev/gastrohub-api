package br.com.fiap.gastrohubapi.presentation.exceptionhandler;

import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RestaurantNotFoundByIdException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(RestaurantAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, UserTypeNotAllowedForRestaurantOwnerException.class})
    public ResponseEntity<Map<String, Object>> handleInvalidArgument(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}
