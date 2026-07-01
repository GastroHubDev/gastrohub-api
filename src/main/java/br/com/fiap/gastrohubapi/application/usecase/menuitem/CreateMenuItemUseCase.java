package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;

public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    public MenuItem execute(MenuItem menuItem) {
        restaurantGateway.findById(menuItem.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundByIdException(
                        "Restaurant not found with id: " + menuItem.getRestaurantId()));
        return menuItemGateway.save(menuItem);
    }
}
