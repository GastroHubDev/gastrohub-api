package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;

import java.util.List;

public class ListRestaurantsUseCase {

    private final RestaurantGateway gateway;

    private ListRestaurantsUseCase(RestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static ListRestaurantsUseCase create(RestaurantGateway gateway) {
        return new ListRestaurantsUseCase(gateway);
    }

    public List<Restaurant> run() {
        return gateway.findAll();
    }
}
