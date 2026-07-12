package br.com.fiap.gastrohubapi.presentation.exceptionhandler;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;
import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RestaurantNotFoundByIdException.class,
            UserNotFoundException.class,
            MenuItemNotFoundException.class,
            UserTypeNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
            RestaurantAlreadyExistsException.class,
            UserAlreadyExistsException.class,
            DuplicateUserTypeNameException.class,
            UserTypeInUseException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            UserTypeNotAllowedForRestaurantOwnerException.class,
            InvalidUserTypeException.class
    })
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


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {

            if (invalidFormatException.getTargetType().equals(BaseCategory.class)) {
                String invalidValue = invalidFormatException.getValue().toString();
                String customMessage = String.format(
                        "The value '%s' is not valid for base category. Allowed values are: OWNER, CLIENT.",
                        invalidValue
                );
                return buildResponse(HttpStatus.BAD_REQUEST, customMessage);
            }

            else if (invalidFormatException.getTargetType().equals(KitchenType.class)) {
                String invalidValue = invalidFormatException.getValue().toString();
                String customMessage = String.format(
                        "The value '%s' is not valid for kitchen type. Allowed values are: ITALIAN, JAPANESE, BRAZILIAN, MEXICAN, INDIAN.",
                        invalidValue
                );
                return buildResponse(HttpStatus.BAD_REQUEST, customMessage);
            }
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request. Please check the request format.");
    }
}