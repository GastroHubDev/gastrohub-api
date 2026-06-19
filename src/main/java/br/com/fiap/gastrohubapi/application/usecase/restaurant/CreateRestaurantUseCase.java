package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.IUserGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;

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
        final Restaurant existingRestaurant = restaurantGateway.findByName(newRestaurantInput.name());

        if (existingRestaurant != null) {
            throw new RestaurantAlreadyExistsException("Some restaurant already exists with this name: " + newRestaurantInput.name());
        }

        final User existingUser = userGateway.findById(newRestaurantInput.restaurantOwnerId());

        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (existingUser.isClient()) {
            throw new UserTypeNotAllowedForRestaurantOwnerException("The restaurantOwnerId cannot be a CLIENT BaseCategory User");
        }

        final Restaurant newRestaurant = Restaurant.create(
                newRestaurantInput.name(),
                newRestaurantInput.address(),
                newRestaurantInput.kitchenType(),
                newRestaurantInput.openingHours(),
                newRestaurantInput.restaurantOwnerId()
        );

        return restaurantGateway.create(newRestaurant);
    }
}