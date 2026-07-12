package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRestaurantRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Address is required.")
        String address,

        @NotNull(message = "Kitchen type is required.")
        KitchenType kitchenType,

        @NotBlank(message = "Opening hours is required.")
        String openingHours,

        @NotNull(message = "Restaurant owner id is required.")
        UUID restaurantOwnerId
) {
}
