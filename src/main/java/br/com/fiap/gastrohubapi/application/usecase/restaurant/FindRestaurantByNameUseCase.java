package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;

import java.util.List;

public class FindRestaurantByNameUseCase {

    private final RestaurantGateway restaurantGateway;

    private FindRestaurantByNameUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public static FindRestaurantByNameUseCase create(RestaurantGateway restaurantGateway) {
        return new FindRestaurantByNameUseCase(restaurantGateway);
    }

    public List<Restaurant> run(String name) {
        return restaurantGateway.findByName(name);
    }
}
