package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.validator.RestaurantDuplicationValidator;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.*;

import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;
    private final RestaurantDuplicationValidator duplicationValidator;

    private UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
        this.duplicationValidator = RestaurantDuplicationValidator.create(restaurantGateway);
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

        duplicationValidator.assertNoDuplicate(
                updateRestaurantInput.name(),
                updateRestaurantInput.restaurantOwnerId(),
                updateRestaurantInput.address(),
                updateRestaurantInput.kitchenType(),
                uuid
        );

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