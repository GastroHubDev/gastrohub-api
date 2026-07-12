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
        Restaurant restaurant = Restaurant.restore(UUID.randomUUID(), "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", UUID.randomUUID());
        when(gateway.findByName("Cantina da Nona")).thenReturn(List.of(restaurant));

        List<Restaurant> result = useCase.run("Cantina da Nona");

        assertThat(result).containsExactly(restaurant);
    }

    @Test
    void shouldReturnEmptyListWhenNoneMatch() {
        when(gateway.findByName("Inexistente")).thenReturn(List.of());

        List<Restaurant> result = useCase.run("Inexistente");

        assertThat(result).isEmpty();
    }
}
