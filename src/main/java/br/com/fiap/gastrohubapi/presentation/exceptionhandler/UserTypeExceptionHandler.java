package br.com.fiap.gastrohubapi.presentation.exceptionhandler;

import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;
import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class UserTypeExceptionHandler {

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserTypeNotFound(UserTypeNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
            DuplicateUserTypeNameException.class,
            UserTypeInUseException.class
    })
    public ResponseEntity<Map<String, Object>> handleUserTypeConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidUserTypeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUserType(InvalidUserTypeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
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