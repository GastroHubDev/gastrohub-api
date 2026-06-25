package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;

import java.util.List;
import java.util.UUID;

public class FindMenuItemsByRestaurantUseCase {

    private final MenuItemGateway menuItemGateway;

    public FindMenuItemsByRestaurantUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemGateway.findAllByRestaurantId(restaurantId);
    }
}
