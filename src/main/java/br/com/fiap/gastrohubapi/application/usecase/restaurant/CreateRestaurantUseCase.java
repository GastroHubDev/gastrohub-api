package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.presentation.dto.request.NewRestaurantDTO;

public class CreateRestaurantUseCase {
    private final IRestaurantGateway gateway;

    private CreateRestaurantUseCase(IRestaurantGateway gateway) {
        this.gateway = gateway;
    }

    public static CreateRestaurantUseCase create(IRestaurantGateway gateway) {
        return new CreateRestaurantUseCase(gateway);
    }

    public Restaurant run(NewRestaurantDTO newRestaurantDTO) throws RestaurantAlreadyExistsException {
        final Restaurant existingRestaurant = gateway.findById(newRestaurantDTO.id());

        if (existingRestaurant != null) {
            throw new RestaurantAlreadyExistsException(newRestaurantDTO.name());
        }

        final Restaurant newRestaurant = Restaurant.create(
                newRestaurantDTO.id(),
                newRestaurantDTO.name(),
                newRestaurantDTO.address(),
                newRestaurantDTO.kitchenType(),
                newRestaurantDTO.openingHours(),
                newRestaurantDTO.restaurantOwner()
        );

        Restaurant restaurant = gateway.create(newRestaurant);
        return restaurant;
    }
}
