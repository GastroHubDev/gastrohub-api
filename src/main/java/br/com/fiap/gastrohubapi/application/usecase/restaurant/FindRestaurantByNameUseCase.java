package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;

import java.util.List;

public class FindRestaurantByNameUseCase {

    private final RestaurantGateway gateway;

    private FindRestaurantByNameUseCase(RestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static FindRestaurantByNameUseCase create(RestaurantGateway gateway) {
        return new FindRestaurantByNameUseCase(gateway);
    }

    public List<Restaurant> run(String name) {
        return gateway.findByName(name);
    }
}