package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;

import java.util.List;

public class FindRestaurantByNameUseCase {

    private final IRestaurantGateway gateway;

    private FindRestaurantByNameUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static FindRestaurantByNameUseCase create(IRestaurantGateway gateway) {
        return new FindRestaurantByNameUseCase(gateway);
    }

    public List<Restaurant> run(String name) {
        return gateway.findByName(name);
    }
}