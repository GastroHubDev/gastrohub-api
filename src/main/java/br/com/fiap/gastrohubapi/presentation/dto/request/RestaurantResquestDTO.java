package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;

import java.util.UUID;

public record RestaurantResquestDTO(String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {
}
