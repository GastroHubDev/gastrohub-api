package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantGateway restaurantGateway;

    private DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public static DeleteRestaurantUseCase create(RestaurantGateway restaurantGateway) {
        return new DeleteRestaurantUseCase(restaurantGateway);
    }

    public void run(UUID uuid) {
        if (!restaurantGateway.existsById(uuid)) {
            throw new RestaurantNotFoundByIdException("Restaurant id: " + uuid);
        }

        restaurantGateway.delete(uuid);
    }
}
