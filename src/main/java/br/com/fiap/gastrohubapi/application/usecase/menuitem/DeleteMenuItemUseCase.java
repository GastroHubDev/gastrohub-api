package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;

import java.util.UUID;

public class DeleteMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public void execute(UUID id) {
        if (!menuItemGateway.existsById(id)) {
            throw new MenuItemNotFoundException(id);
        }
        menuItemGateway.delete(id);
    }
}
