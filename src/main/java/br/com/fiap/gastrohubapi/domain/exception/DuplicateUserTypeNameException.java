package br.com.fiap.gastrohubapi.domain.exception;

public class DuplicateUserTypeNameException extends RuntimeException {

    public DuplicateUserTypeNameException(String message) {
        super(message);
    }
}
