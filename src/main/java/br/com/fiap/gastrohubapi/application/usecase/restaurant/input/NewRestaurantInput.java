package br.com.fiap.gastrohubapi.application.usecase.restaurant.input;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;

import java.util.UUID;

public record NewRestaurantInput(String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {
}
