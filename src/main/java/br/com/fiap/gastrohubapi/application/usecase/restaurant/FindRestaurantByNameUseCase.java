package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByNameException;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;

public class FindRestaurantByNameUseCase {

    private final IRestaurantGateway gateway;

    private FindRestaurantByNameUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static FindRestaurantByNameUseCase create(IRestaurantGateway gateway) {
        return new FindRestaurantByNameUseCase(gateway);
    }

    public Restaurant run(String name) throws RestaurantNotFoundByNameException {
        Restaurant restaurant = gateway.findByName(name);
        if (restaurant == null) {
            throw new RestaurantNotFoundByNameException("Restaurant name: " + name);
        }

        return restaurant;
    }
}