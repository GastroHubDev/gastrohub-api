package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final RestaurantGateway gateway;

    private DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.gateway = restaurantGateway;
    }

    public static DeleteRestaurantUseCase create(RestaurantGateway gateway) {
        return new DeleteRestaurantUseCase(gateway);
    }

    public void run(UUID uuid) {
        gateway.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundByIdException("Restaurant id: " + uuid));

        gateway.delete(uuid);
    }
}
