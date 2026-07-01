package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.validator.RestaurantDuplicationValidator;

public class CreateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;
    private final RestaurantDuplicationValidator duplicationValidator;

    private CreateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
        this.duplicationValidator = RestaurantDuplicationValidator.create(restaurantGateway);
    }

    public static CreateRestaurantUseCase create(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new CreateRestaurantUseCase(restaurantGateway, userGateway);
    }

    public Restaurant run(NewRestaurantInput newRestaurantInput) {
        final User existingUser = userGateway.findById(newRestaurantInput.restaurantOwnerId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (existingUser.isClient()) {
            throw new UserTypeNotAllowedForRestaurantOwnerException("The restaurantOwnerId cannot be a CLIENT BaseCategory User");
        }

        duplicationValidator.assertNoDuplicate(
                newRestaurantInput.name(),
                newRestaurantInput.restaurantOwnerId(),
                newRestaurantInput.address(),
                newRestaurantInput.kitchenType(),
                null
        );

        Restaurant restaurant = Restaurant.create(
                newRestaurantInput.name(),
                newRestaurantInput.address(),
                newRestaurantInput.kitchenType(),
                newRestaurantInput.openingHours(),
                newRestaurantInput.restaurantOwnerId()
        );

        return restaurantGateway.save(restaurant);
    }
}