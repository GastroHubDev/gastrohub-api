package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.IUserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.*;

import java.util.List;
import java.util.UUID;

public class UpdateRestaurantUseCase {
    private final IRestaurantGateway restaurantGateway;
    private final IUserGateway userGateway;

    private UpdateRestaurantUseCase(IRestaurantGateway restaurantGateway, IUserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    public static UpdateRestaurantUseCase create(IRestaurantGateway restaurantGateway, IUserGateway userGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway, userGateway);
    }

    public Restaurant run(UUID uuid, UpdateRestaurantInput updateRestaurantInput) {
        restaurantGateway.findById(uuid)
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

        return restaurantGateway.update(uuid, updateRestaurantInput);
    }

}
