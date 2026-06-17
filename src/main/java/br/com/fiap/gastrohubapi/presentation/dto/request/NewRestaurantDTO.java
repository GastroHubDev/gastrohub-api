package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;

import java.util.UUID;

public record NewRestaurantDTO(UUID id, String name, String address, KitchenType kitchenType, String openingHours, User restaurantOwner) {
}
