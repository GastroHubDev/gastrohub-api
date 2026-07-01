package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;

import java.util.UUID;

public class FindRestaurantByIdUseCase {
    private final RestaurantGateway restaurantGateway;

    private FindRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public static FindRestaurantByIdUseCase create(RestaurantGateway restaurantGateway) {
        return new FindRestaurantByIdUseCase(restaurantGateway);
    }

    public Restaurant run(UUID uuid) {
        return restaurantGateway.findById(uuid)
                .orElseThrow(() -> new RestaurantNotFoundByIdException("Restaurant id: " + uuid));
    }
}
