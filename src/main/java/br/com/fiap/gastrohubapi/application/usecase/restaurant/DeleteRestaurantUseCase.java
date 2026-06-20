package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;

import java.util.UUID;

public class DeleteRestaurantUseCase {
    private final IRestaurantGateway gateway;

    private DeleteRestaurantUseCase(IRestaurantGateway restaurantGateway) {
        this.gateway = restaurantGateway;
    }

    public static DeleteRestaurantUseCase create(IRestaurantGateway gateway) {
        return new DeleteRestaurantUseCase(gateway);
    }

    public void run(UUID uuid) {
        gateway.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundByIdException("Restaurant id: " + uuid));

        gateway.delete(uuid);
    }
}
