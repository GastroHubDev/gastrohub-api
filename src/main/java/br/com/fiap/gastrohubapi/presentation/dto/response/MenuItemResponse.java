package br.com.fiap.gastrohubapi.presentation.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean onlyInRestaurant,
        String photoPath,
        UUID restaurantId
) {}