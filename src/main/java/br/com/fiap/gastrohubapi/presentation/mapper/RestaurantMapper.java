package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponse;

import java.util.List;

public class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getKitchenType(),
                restaurant.getOpeningHours(),
                restaurant.getRestaurantOwnerId()
        );
    }

    public static List<RestaurantResponse> toResponseList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    }
}
