package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.MenuItemResponse;

public class MenuItemMapper {

    private MenuItemMapper() {}

    public static MenuItem toDomain(CreateMenuItemRequest request) {
        return MenuItem.create(request.name(), request.description(),
                request.price(), request.onlyInRestaurant(),
                request.photoPath(), request.restaurantId());
    }

    public static MenuItem toDomain(UpdateMenuItemRequest request) {
        return MenuItem.create(request.name(), request.description(),
                request.price(), request.onlyInRestaurant(),
                request.photoPath(), request.restaurantId());
    }

    public static MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(menuItem.getId(), menuItem.getName(),
                menuItem.getDescription(), menuItem.getPrice(),
                menuItem.isOnlyInRestaurant(), menuItem.getPhotoPath(),
                menuItem.getRestaurantId());
    }
}