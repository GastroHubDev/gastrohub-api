package br.com.fiap.gastrohubapi.presentation.dto.response;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;

import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String address,
        KitchenType kitchenType,
        String openingHours,
        UUID restaurantOwnerId
) {
}
