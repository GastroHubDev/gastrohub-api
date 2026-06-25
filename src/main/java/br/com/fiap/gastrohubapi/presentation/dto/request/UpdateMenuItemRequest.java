package br.com.fiap.gastrohubapi.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMenuItemRequest(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal price,

        boolean onlyInRestaurant,

        String photoPath,

        @NotNull
        UUID restaurantId
) {}