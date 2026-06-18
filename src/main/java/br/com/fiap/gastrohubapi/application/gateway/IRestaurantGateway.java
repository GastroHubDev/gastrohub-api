package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;

import java.util.UUID;

public interface IRestaurantGateway {
    Restaurant findById(UUID uuid);
    Restaurant findByName(String name);
    Restaurant delete(UUID uuid);
    Restaurant create(String name, String address, KitchenType kitchenType, String openingHours, User restaurantOwner);
    Restaurant create(Restaurant restaurant);
    Restaurant update(String name, String address, KitchenType kitchenType, String openingHours, User restaurantOwner);
}
