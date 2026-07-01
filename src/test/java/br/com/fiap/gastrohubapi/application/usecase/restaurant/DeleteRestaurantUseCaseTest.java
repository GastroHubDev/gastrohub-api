package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private DeleteRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = DeleteRestaurantUseCase.create(restaurantGateway);
    }

    @Test
    void shouldDeleteWhenRestaurantExists() {
        UUID id = UUID.randomUUID();
        when(restaurantGateway.existsById(id)).thenReturn(true);

        useCase.run(id);

        verify(restaurantGateway).delete(id);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        UUID id = UUID.randomUUID();
        when(restaurantGateway.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> useCase.run(id))
                .isInstanceOf(RestaurantNotFoundByIdException.class);

        verify(restaurantGateway, never()).delete(any());
    }
}
