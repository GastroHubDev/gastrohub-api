package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class DeleteRestaurantUseCase {
    private final RestaurantGateway gateway;

    private DeleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.gateway = restaurantGateway;
    }

    public static DeleteRestaurantUseCase create(RestaurantGateway gateway) {
        return new DeleteRestaurantUseCase(gateway);
    }

    public void run(UUID uuid) {
        if (!gateway.existsById(uuid)) {
            throw new RestaurantNotFoundByIdException("Restaurant id: " + uuid);
        }

        gateway.delete(uuid);
    }
}
