package br.com.fiap.gastrohubapi.domain.exception;

public class InvalidUserTypeException extends RuntimeException {

    public InvalidUserTypeException(String message) {
        super(message);
    }
}