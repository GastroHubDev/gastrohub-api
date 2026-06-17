package br.com.fiap.gastrohubapi.application.usecase;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.interfaces.IRestaurantGateway;

import java.util.UUID;

public class FindRestaurantByIdUseCase {
    private final IRestaurantGateway gateway;

    private FindRestaurantByIdUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static FindRestaurantByIdUseCase create(IRestaurantGateway gateway) {
        return new FindRestaurantByIdUseCase(gateway);
    }

    public Restaurant run(UUID uuid) throws RestaurantNotFoundByIdException {
        Restaurant restaurant = gateway.findById(uuid);
        if (restaurant == null) {
            throw new RestaurantNotFoundByIdException("Restaurant id: " + uuid);
        }

        return restaurant;
    }
}
