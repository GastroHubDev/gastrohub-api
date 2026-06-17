package br.com.fiap.gastrohubapi.domain.exception;

public class RestaurantNotFoundByNameException extends RuntimeException {
    public RestaurantNotFoundByNameException(String message) {
        super(message);
    }
}
