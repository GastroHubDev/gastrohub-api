package br.com.fiap.gastrohubapi.domain.exception;

public class UserTypeInUseException extends RuntimeException {

    public UserTypeInUseException(String message) {
        super(message);
    }
}