package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.DeleteRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByNameUseCase;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.infrastructure.persistence.gateway.RestaurantGatewayImpl;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.RestaurantJpaRepository;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponseDTO;
import br.com.fiap.gastrohubapi.presentation.mapper.RestaurantMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Restaurant", description = "Restaurant management endpoints")
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantJpaRepository repository;

    private RestaurantController(RestaurantJpaRepository repository) {
        this.repository = repository;
    }

    public static RestaurantController create(RestaurantJpaRepository repository) {
        return new RestaurantController(repository);
    }

    @Operation(summary = "Find a restaurant by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/{id}")
    public RestaurantResponseDTO findById(UUID uuid) {
        RestaurantGateway gateway = RestaurantGatewayImpl.create(repository);
        FindRestaurantByIdUseCase useCase = FindRestaurantByIdUseCase.create(gateway);

        Restaurant restaurant = useCase.run(uuid);

        return RestaurantMapper.toResponse(restaurant);
    }

    @Operation(summary = "List all restaurants by name")
    @ApiResponse(responseCode = "200", description = "Listed restaurants filtered by name")
    @GetMapping
    public List<RestaurantResponseDTO> findByName(String name) {
        RestaurantGateway gateway = RestaurantGatewayImpl.create(repository);
        FindRestaurantByNameUseCase useCase = FindRestaurantByNameUseCase.create(gateway);
        return RestaurantMapper.toResponseList(useCase.run(name));
    }

    @Operation(summary = "Delete a restaurant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant deleted"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "409", description = "Restaurant is in use")
    })
    @DeleteMapping("/{id}")
    public void delete(UUID uuid) {
        RestaurantGateway gateway = RestaurantGatewayImpl.create(repository);
        DeleteRestaurantUseCase useCase = DeleteRestaurantUseCase.create(gateway);
        useCase.run(uuid);
    }

}
