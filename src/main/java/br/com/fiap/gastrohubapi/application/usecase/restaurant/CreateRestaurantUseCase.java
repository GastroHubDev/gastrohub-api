package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.gateway.IRestaurantGateway;
import br.com.fiap.gastrohubapi.presentation.dto.request.NewRestaurantDTO;

public class CreateRestaurantUseCase {
    private final IRestaurantGateway restaurantGateway;

    private CreateRestaurantUseCase(IRestaurantGateway gateway) {
        this.restaurantGateway = gateway;
    }

    public static CreateRestaurantUseCase create(IRestaurantGateway gateway) {
        return new CreateRestaurantUseCase(gateway);
    }

    public Restaurant run(NewRestaurantDTO newRestaurantDTO) throws RestaurantAlreadyExistsException {
        final Restaurant existingRestaurant = restaurantGateway.findById(newRestaurantDTO.id());

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

        Restaurant restaurant = restaurantGateway.create(newRestaurant);
        return restaurant;
    }
}
