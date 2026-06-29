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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway gateway;

    private DeleteRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = DeleteRestaurantUseCase.create(gateway);
    }

    @Test
    void shouldDeleteWhenRestaurantExists() {
        UUID id = UUID.randomUUID();
        Restaurant restaurant = Restaurant.restore(id, "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", UUID.randomUUID());
        when(gateway.findById(id)).thenReturn(Optional.of(restaurant));

        useCase.run(id);

        verify(gateway).delete(id);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(id))
                .isInstanceOf(RestaurantNotFoundByIdException.class);

        verify(gateway, never()).delete(id);
    }
}
