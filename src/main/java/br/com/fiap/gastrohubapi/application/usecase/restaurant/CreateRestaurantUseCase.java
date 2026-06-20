package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.IUserGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;

import java.util.List;

public class CreateRestaurantUseCase {
    private final IRestaurantGateway restaurantGateway;
    private final IUserGateway userGateway;

    private CreateRestaurantUseCase(IRestaurantGateway restaurantGateway, IUserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public static CreateRestaurantUseCase create(IRestaurantGateway restaurantGateway, IUserGateway userGateway) {
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

        return restaurantGateway.create(newRestaurantInput);
    }
}