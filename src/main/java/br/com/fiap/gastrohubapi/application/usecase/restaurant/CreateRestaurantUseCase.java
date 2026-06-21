package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;

import java.util.List;

public class CreateRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    private CreateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
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

        final List<Restaurant> restaurants = restaurantGateway.findByName(newRestaurantInput.name());

        final boolean restaurantAlreadyExists = restaurants.stream().anyMatch(r ->
                r.getRestaurantOwnerId().equals(newRestaurantInput.restaurantOwnerId())
                && r.getAddress().equals(newRestaurantInput.address())
                && r.getKitchenType().equals(newRestaurantInput.kitchenType())
        );

        if (restaurantAlreadyExists) {
            throw new RestaurantAlreadyExistsException("Another restaurant already exists with the same name, owner, address and kitchentype");
        }

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