package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;

public class FindMenuItemByIdUseCase {
    private final MenuItemGateway menuItemGateway;

    public FindMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(Long id) {
        return menuItemGateway.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
    }
}
