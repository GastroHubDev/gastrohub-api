package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponse;

import java.util.List;

public class RestaurantMapper {

    private RestaurantMapper() {
    }

    public static NewRestaurantInput toInput(CreateRestaurantRequest request) {
        return new NewRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );
    }

    public static UpdateRestaurantInput toInput(UpdateRestaurantRequest request) {
        return new UpdateRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );
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
