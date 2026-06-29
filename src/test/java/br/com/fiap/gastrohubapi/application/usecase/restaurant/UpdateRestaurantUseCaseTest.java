package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
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
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private User owner;

    private UpdateRestaurantUseCase useCase;

    private static final String NEW_NAME = "Sushi House";
    private static final String NEW_ADDRESS = "Av. Central, 99";
    private static final KitchenType NEW_KITCHEN_TYPE = KitchenType.JAPANESE;
    private static final String NEW_OPENING_HOURS = "11:00-23:00";

    private UUID restaurantId;
    private UUID ownerId;
    private Restaurant existing;
    private UpdateRestaurantInput input;

    @BeforeEach
    void setUp() {
        useCase = UpdateRestaurantUseCase.create(restaurantGateway, userGateway);
        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        existing = Restaurant.restore(restaurantId, "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", ownerId);
        input = new UpdateRestaurantInput(NEW_NAME, NEW_ADDRESS, NEW_KITCHEN_TYPE, NEW_OPENING_HOURS, ownerId);
    }

    @Test
    void shouldUpdateRestaurantWhenValid() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(false);
        when(restaurantGateway.findByName(NEW_NAME)).thenReturn(List.of());

        Restaurant result = useCase.run(restaurantId, input);

        assertThat(result.getId()).isEqualTo(restaurantId);
        assertThat(result.getName()).isEqualTo(NEW_NAME);
        assertThat(result.getAddress()).isEqualTo(NEW_ADDRESS);
        assertThat(result.getKitchenType()).isEqualTo(NEW_KITCHEN_TYPE);
        assertThat(result.getOpeningHours()).isEqualTo(NEW_OPENING_HOURS);
        verify(restaurantGateway).update(existing);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(RestaurantNotFoundByIdException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenOwnerNotFound() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenOwnerIsClient() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(true);

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(UserTypeNotAllowedForRestaurantOwnerException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenDuplicateBelongsToAnotherRestaurant() {
        Restaurant duplicate = Restaurant.restore(UUID.randomUUID(), NEW_NAME, NEW_ADDRESS,
                NEW_KITCHEN_TYPE, NEW_OPENING_HOURS, ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(false);
        when(restaurantGateway.findByName(NEW_NAME)).thenReturn(List.of(duplicate));

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(RestaurantAlreadyExistsException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldIgnoreItselfWhenCheckingDuplicate() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.isClient()).thenReturn(false);
        // A propria entidade, ja com os dados novos, aparece em findByName mas deve ser ignorada (mesmo id).
        Restaurant itself = Restaurant.restore(restaurantId, NEW_NAME, NEW_ADDRESS,
                NEW_KITCHEN_TYPE, NEW_OPENING_HOURS, ownerId);
        when(restaurantGateway.findByName(NEW_NAME)).thenReturn(List.of(itself));

        Restaurant result = useCase.run(restaurantId, input);

        assertThat(result.getName()).isEqualTo(NEW_NAME);
        verify(restaurantGateway).update(existing);
    }
}
