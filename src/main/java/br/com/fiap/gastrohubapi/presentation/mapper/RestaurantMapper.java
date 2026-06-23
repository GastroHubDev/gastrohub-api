package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponseDTO;

import java.util.List;

public class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static RestaurantResponseDTO toResponse(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getKitchenType(),
                restaurant.getOpeningHours(),
                restaurant.getRestaurantOwnerId()
        );
    }

    public static List<RestaurantResponseDTO> toResponseList(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantMapper::toResponse).toList();
    }

}
