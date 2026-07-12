package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;

import java.util.UUID;

public class UpdateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;

    public UpdateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(UUID id, MenuItem updatedData) {
        if (!menuItemGateway.existsById(id)) {
            throw new MenuItemNotFoundException(id);
        }
        MenuItem menuItem = MenuItem.restore(
                id,
                updatedData.getName(),
                updatedData.getDescription(),
                updatedData.getPrice(),
                updatedData.isOnlyInRestaurant(),
                updatedData.getPhotoPath(),
                updatedData.getRestaurantId()
        );
        return menuItemGateway.update(menuItem);
    }
}
