package br.com.fiap.gastrohubapi.domain.exception;

public class RestaurantNotFoundByIdException extends RuntimeException {
    public RestaurantNotFoundByIdException(String message) {
        super(message);
    }
}
