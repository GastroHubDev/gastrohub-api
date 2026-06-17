package br.com.fiap.gastrohubapi.application.usecase;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;


public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public CreateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(MenuItem menuItem) {
        return menuItemGateway.save(menuItem);
    }
}

