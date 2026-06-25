package br.com.fiap.gastrohubapi.domain.exception;

public class UserTypeNotAllowedForRestaurantOwnerException extends RuntimeException {
    public UserTypeNotAllowedForRestaurantOwnerException(String message) {
        super(message);
    }
}
