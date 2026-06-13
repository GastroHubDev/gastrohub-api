package br.com.fiap.gastrohubapi.domain.exception;

public class UserTypeNotFoundException extends RuntimeException {

    public UserTypeNotFoundException(String message) {
        super(message);
    }
}