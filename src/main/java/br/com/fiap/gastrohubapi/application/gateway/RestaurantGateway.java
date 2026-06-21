package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {
    Optional<Restaurant> findById(UUID uuid);
    List<Restaurant> findByName(String name);
    void delete(UUID uuid);
    Restaurant save(Restaurant restaurant);
    Restaurant update(Restaurant restaurant);
}
