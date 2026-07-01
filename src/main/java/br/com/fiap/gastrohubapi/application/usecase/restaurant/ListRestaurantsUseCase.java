package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;

import java.util.List;

public class ListRestaurantsUseCase {

    private final RestaurantGateway restaurantGateway;

    private ListRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public static ListRestaurantsUseCase create(RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    public List<Restaurant> run() {
        return restaurantGateway.findAll();
    }
}
