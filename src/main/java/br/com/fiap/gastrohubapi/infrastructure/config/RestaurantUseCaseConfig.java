package br.com.fiap.gastrohubapi.infrastructure.config;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {
    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return CreateRestaurantUseCase.create(restaurantGateway, userGateway);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return UpdateRestaurantUseCase.create(restaurantGateway, userGateway);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return FindRestaurantByIdUseCase.create(restaurantGateway);
    }

    @Bean
    public FindRestaurantByNameUseCase findRestaurantByNameUseCase(RestaurantGateway restaurantGateway) {
        return FindRestaurantByNameUseCase.create(restaurantGateway);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return ListRestaurantsUseCase.create(restaurantGateway);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return DeleteRestaurantUseCase.create(restaurantGateway);
    }
}
