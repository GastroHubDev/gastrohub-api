package br.com.fiap.gastrohubapi.presentation.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Restaurants", description = "Restaurant management endpoints")
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final FindRestaurantByNameUseCase findRestaurantByNameUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindRestaurantByNameUseCase findRestaurantByNameUseCase,
            ListRestaurantsUseCase listRestaurantsUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase
    ) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findRestaurantByNameUseCase = findRestaurantByNameUseCase;
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
    }

    @Operation(summary = "Create a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant created"),
            @ApiResponse(responseCode = "400", description = "Invalid request or owner is a CLIENT user"),
            @ApiResponse(responseCode = "404", description = "Restaurant owner (user) not found"),
            @ApiResponse(responseCode = "409", description = "Restaurant already exists with the same name, owner, address and kitchen type")
    })
    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody CreateRestaurantRequest request) {
        final NewRestaurantInput input = new NewRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );

        final Restaurant restaurant = createRestaurantUseCase.run(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantMapper.toResponse(restaurant));
    }

    @Operation(summary = "Find a restaurant by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable UUID id) {
        final Restaurant restaurant = findRestaurantByIdUseCase.run(id);

        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @Operation(summary = "Search restaurants by name")
    @ApiResponse(responseCode = "200", description = "Restaurants listed")
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> findByName(@RequestParam String name) {
        final List<Restaurant> restaurants = findRestaurantByNameUseCase.run(name);

        return ResponseEntity.ok(RestaurantMapper.toResponseList(restaurants));
    }

    @Operation(summary = "List all restaurants")
    @ApiResponse(responseCode = "200", description = "Restaurants listed")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        final List<Restaurant> restaurants = listRestaurantsUseCase.run();

        return ResponseEntity.ok(RestaurantMapper.toResponseList(restaurants));
    }

    @Operation(summary = "Update a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request or owner is a CLIENT user"),
            @ApiResponse(responseCode = "404", description = "Restaurant or owner (user) not found"),
            @ApiResponse(responseCode = "409", description = "Another restaurant already exists with the same name, owner, address and kitchen type")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateRestaurantRequest request) {
        final UpdateRestaurantInput input = new UpdateRestaurantInput(
                request.name(),
                request.address(),
                request.kitchenType(),
                request.openingHours(),
                request.restaurantOwnerId()
        );

        final Restaurant restaurant = updateRestaurantUseCase.run(id, input);

        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @Operation(summary = "Delete a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deleted"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteRestaurantUseCase.run(id);

        return ResponseEntity.noContent().build();
    }
}
