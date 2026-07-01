package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
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

    private UpdateRestaurantUseCase useCase;

    private UUID restaurantId;
    private UUID ownerId;
    private User owner;
    private Restaurant existingRestaurant;
    private UpdateRestaurantInput input;

    @BeforeEach
    void setUp() {
        useCase = UpdateRestaurantUseCase.create(restaurantGateway, userGateway);

        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        UserType ownerType = new UserType(1L, "Owner", BaseCategory.OWNER);
        owner = User.restore(ownerId, "Caue", "caue@test.com", "senha123", ownerType);

        existingRestaurant = Restaurant.restore(restaurantId, "Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);

        input = new UpdateRestaurantInput("Sushi Bom", "Rua 2, 456",
                KitchenType.ITALIAN, "11:00-23:00", ownerId);
    }

    @Test
    void shouldUpdateRestaurantWhenValid() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.findByName(input.name())).thenReturn(List.of());
        when(restaurantGateway.update(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Restaurant result = useCase.run(restaurantId, input);

        assertThat(result.getName()).isEqualTo("Sushi Bom");
        assertThat(result.getAddress()).isEqualTo("Rua 2, 456");
        verify(restaurantGateway).update(any(Restaurant.class));
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(RestaurantNotFoundByIdException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenOwnerDoesNotExist() {
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(UserNotFoundException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenOwnerIsClient() {
        UserType clientType = new UserType(2L, "Client", BaseCategory.CLIENT);
        User client = User.restore(ownerId, "Paula", "paula@test.com", "senha123", clientType);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(client));

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(UserTypeNotAllowedForRestaurantOwnerException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenAnotherRestaurantWithSameDataExists() {
        Restaurant other = Restaurant.restore(UUID.randomUUID(), input.name(), input.address(),
                input.kitchenType(), input.openingHours(), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.findByName(input.name())).thenReturn(List.of(other));

        assertThatThrownBy(() -> useCase.run(restaurantId, input))
                .isInstanceOf(RestaurantAlreadyExistsException.class);

        verify(restaurantGateway, never()).update(any());
    }

    @Test
    void shouldNotThrowWhenDuplicateCheckMatchesTheRestaurantBeingUpdated() {
        Restaurant sameRestaurantWithNewData = Restaurant.restore(restaurantId, input.name(), input.address(),
                input.kitchenType(), input.openingHours(), ownerId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.findByName(input.name())).thenReturn(List.of(sameRestaurantWithNewData));
        when(restaurantGateway.update(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Restaurant result = useCase.run(restaurantId, input);

        assertThat(result.getName()).isEqualTo(input.name());
    }
}
