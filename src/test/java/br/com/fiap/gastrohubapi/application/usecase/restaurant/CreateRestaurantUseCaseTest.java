package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private User owner;

    private CreateRestaurantUseCase useCase;

    private static final String NAME = "Cantina da Nona";
    private static final String ADDRESS = "Rua das Flores, 123";
    private static final KitchenType KITCHEN_TYPE = KitchenType.ITALIAN;
    private static final String OPENING_HOURS = "08:00-22:00";

    private UUID ownerId;
    private NewRestaurantInput input;

    @BeforeEach
    void setUp() {
        useCase = CreateRestaurantUseCase.create(restaurantGateway, userGateway);
        ownerId = UUID.randomUUID();
        input = new NewRestaurantInput(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);
    }

    @Test
    void shouldCreateRestaurantWhenOwnerIsValidAndNoDuplicate() {
        Restaurant saved = Restaurant.restore(UUID.randomUUID(), NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);

        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(false);
        when(restaurantGateway.findByName(NAME)).thenReturn(List.of());
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(saved);

        Restaurant result = useCase.run(input);

        assertThat(result).isSameAs(saved);
        verify(restaurantGateway).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenOwnerIsClient() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(true);

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(UserTypeNotAllowedForRestaurantOwnerException.class);

        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenDuplicateExists() {
        Restaurant duplicate = Restaurant.restore(UUID.randomUUID(), NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);

        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(false);
        when(restaurantGateway.findByName(NAME)).thenReturn(List.of(duplicate));

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(RestaurantAlreadyExistsException.class);

        verify(restaurantGateway, never()).save(any());
    }
}
