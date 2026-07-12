package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantGateway gateway;

    private FindRestaurantByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = FindRestaurantByIdUseCase.create(gateway);
    }

    @Test
    void shouldReturnRestaurantWhenFound() {
        UUID id = UUID.randomUUID();
        Restaurant restaurant = Restaurant.restore(id, "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", UUID.randomUUID());
        when(gateway.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = useCase.run(id);

        assertThat(result).isSameAs(restaurant);
    }

    @Test
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(id))
                .isInstanceOf(RestaurantNotFoundByIdException.class)
                .hasMessageContaining(id.toString());
    }
}
