package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByNameUseCaseTest {

    @Mock
    private RestaurantGateway gateway;

    private FindRestaurantByNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = FindRestaurantByNameUseCase.create(gateway);
    }

    @Test
    void shouldReturnMatchingRestaurants() {
        String name = "Restaurante Bom";
        Restaurant restaurant = Restaurant.restore(UUID.randomUUID(), name, "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID());
        when(gateway.findByName(name)).thenReturn(List.of(restaurant));

        List<Restaurant> result = useCase.run(name);

        assertThat(result).containsExactly(restaurant);
    }

    @Test
    void shouldReturnEmptyListWhenNoneFound() {
        when(gateway.findByName("Inexistente")).thenReturn(List.of());

        assertThat(useCase.run("Inexistente")).isEmpty();
    }
}
