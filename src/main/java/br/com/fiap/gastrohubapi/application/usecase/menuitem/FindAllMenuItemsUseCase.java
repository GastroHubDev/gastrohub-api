package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;

import java.util.List;

public class FindAllMenuItemsUseCase {
    private final MenuItemGateway menuItemGateway;

    public FindAllMenuItemsUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItem> execute() {
        return menuItemGateway.findAll();
    }
}
