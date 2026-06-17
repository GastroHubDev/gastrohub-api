package br.com.fiap.gastrohubapi.domain.exception;

public class MenuItemNotFoundException extends RuntimeException {

    public MenuItemNotFoundException(Long id) {
        super("Menu item not found with id: " + id);
    }
}
