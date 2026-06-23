package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.CreateRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.DeleteRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByNameUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.ListRestaurantsUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.RestaurantMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public RestaurantController(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody CreateRestaurantRequest request) {
        final NewRestaurantInput input = new NewRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );

        final Restaurant restaurant = CreateRestaurantUseCase.create(restaurantGateway, userGateway).run(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantMapper.toResponse(restaurant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable UUID id) {
        final Restaurant restaurant = FindRestaurantByIdUseCase.create(restaurantGateway).run(id);

        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> findByName(@RequestParam String name) {
        final List<Restaurant> restaurants = FindRestaurantByNameUseCase.create(restaurantGateway).run(name);

        return ResponseEntity.ok(RestaurantMapper.toResponseList(restaurants));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        final List<Restaurant> restaurants = ListRestaurantsUseCase.create(restaurantGateway).run();

        return ResponseEntity.ok(RestaurantMapper.toResponseList(restaurants));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateRestaurantRequest request) {
        final UpdateRestaurantInput input = new UpdateRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );

        final Restaurant restaurant = UpdateRestaurantUseCase.create(restaurantGateway, userGateway).run(id, input);

        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        DeleteRestaurantUseCase.create(restaurantGateway).run(id);

        return ResponseEntity.noContent().build();
    }
}
