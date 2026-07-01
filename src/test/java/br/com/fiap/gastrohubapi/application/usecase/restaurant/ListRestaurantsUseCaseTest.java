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
class ListRestaurantsUseCaseTest {

    @Mock
    private RestaurantGateway gateway;

    private ListRestaurantsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListRestaurantsUseCase.create(gateway);
    }

    @Test
    void shouldReturnAllRestaurants() {
        Restaurant a = Restaurant.restore(UUID.randomUUID(), "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", UUID.randomUUID());
        Restaurant b = Restaurant.restore(UUID.randomUUID(), "Sushi House", "Av. Central, 99",
                KitchenType.JAPANESE, "11:00-23:00", UUID.randomUUID());
        when(gateway.findAll()).thenReturn(List.of(a, b));

        List<Restaurant> result = useCase.run();

        assertThat(result).containsExactly(a, b);
    }

    @Test
    void shouldReturnEmptyListWhenNoneExist() {
        when(gateway.findAll()).thenReturn(List.of());

        List<Restaurant> result = useCase.run();

        assertThat(result).isEmpty();
    }
}
