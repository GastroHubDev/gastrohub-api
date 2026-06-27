package br.com.fiap.gastrohubapi.application.usecase.restaurant.validator;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;

import java.util.List;
import java.util.UUID;

public class RestaurantDuplicationValidator {
    private final RestaurantGateway restaurantGateway;

    private RestaurantDuplicationValidator(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public static RestaurantDuplicationValidator create(RestaurantGateway restaurantGateway) {
        return new RestaurantDuplicationValidator(restaurantGateway);
    }

    public void assertNoDuplicate(String name, UUID ownerId, String address,
                                  KitchenType kitchenType, UUID idToIgnore) {
        final List<Restaurant> restaurants = restaurantGateway.findByName(name);

        final boolean exists = restaurants.stream().anyMatch(r ->
                (idToIgnore == null || !r.getId().equals(idToIgnore))
                && r.getRestaurantOwnerId().equals(ownerId)
                && r.getAddress().equals(address)
                && r.getKitchenType().equals(kitchenType)
        );

        if (exists) {
            throw new RestaurantAlreadyExistsException(
                    "Another restaurant already exists with the same name, owner, address and kitchentype");
        }
    }
}
