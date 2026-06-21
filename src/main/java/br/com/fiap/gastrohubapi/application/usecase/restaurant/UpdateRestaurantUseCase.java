package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.*;

import java.util.List;
import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    private UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public static UpdateRestaurantUseCase create(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway, userGateway);
    }

    public Restaurant run(UUID uuid, UpdateRestaurantInput updateRestaurantInput) {
        Restaurant restaurant = restaurantGateway.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundByIdException("Restaurant not found"));

        User existingUser = userGateway.findById(updateRestaurantInput.restaurantOwnerId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (existingUser.isClient()) {
            throw new UserTypeNotAllowedForRestaurantOwnerException("The restaurantOwnerId cannot be a CLIENT BaseCategory User");
        }

        final List<Restaurant> restaurants = restaurantGateway.findByName(updateRestaurantInput.name());

        final boolean restaurantAlreadyExists = restaurants.stream().anyMatch(r ->
                !r.getId().equals(uuid)
                && r.getRestaurantOwnerId().equals(updateRestaurantInput.restaurantOwnerId())
                && r.getAddress().equals(updateRestaurantInput.address())
                && r.getKitchenType().equals(updateRestaurantInput.kitchenType())
        );

        if (restaurantAlreadyExists) {
            throw new RestaurantAlreadyExistsException("Another restaurant already exists with the same name, owner, address and kitchentype");
        }

        restaurant.update(
                updateRestaurantInput.name(),
                updateRestaurantInput.address(),
                updateRestaurantInput.kitchenType(),
                updateRestaurantInput.openingHours(),
                updateRestaurantInput.restaurantOwnerId()
        );

        restaurantGateway.update(restaurant);

        return restaurant;
    }
}