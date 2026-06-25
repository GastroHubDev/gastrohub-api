package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;

import java.util.UUID;

public class FindRestaurantByIdUseCase {
    private final RestaurantGateway gateway;

    private FindRestaurantByIdUseCase(RestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static FindRestaurantByIdUseCase create(RestaurantGateway gateway) {
        return new FindRestaurantByIdUseCase(gateway);
    }

    public Restaurant run(UUID uuid) {
        return gateway.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundByIdException("Restaurant id: " + uuid));
    }
}
