package br.com.fiap.gastrohubapi.application.usecase.restaurant;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
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

    private CreateRestaurantUseCase useCase;

    private UUID ownerId;
    private User owner;
    private NewRestaurantInput input;

    @BeforeEach
    void setUp() {
        useCase = CreateRestaurantUseCase.create(restaurantGateway, userGateway);

        ownerId = UUID.randomUUID();
        UserType ownerType = new UserType(1L, "Owner", BaseCategory.OWNER);
        owner = User.restore(ownerId, "Caue", "caue@test.com", "senha123", ownerType);

        input = new NewRestaurantInput("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);
    }

    @Test
    void shouldCreateRestaurantWhenOwnerIsValidAndNoDuplicateExists() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.findByName(input.name())).thenReturn(List.of());
        when(restaurantGateway.save(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Restaurant result = useCase.run(input);

        assertThat(result.getName()).isEqualTo("Restaurante Bom");
        assertThat(result.getRestaurantOwnerId()).isEqualTo(ownerId);
        verify(restaurantGateway).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowWhenOwnerDoesNotExist() {
        when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(UserNotFoundException.class);

        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenOwnerIsClient() {
        UserType clientType = new UserType(2L, "Client", BaseCategory.CLIENT);
        User client = User.restore(ownerId, "Rodrigo", "rodrigo@test.com", "senha123", clientType);
        when(userGateway.findById(ownerId)).thenReturn(Optional.of(client));

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(UserTypeNotAllowedForRestaurantOwnerException.class);

        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenDuplicateRestaurantExists() {
        Restaurant existing = Restaurant.create(input.name(), input.address(), input.kitchenType(),
                input.openingHours(), ownerId);

        when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
        when(restaurantGateway.findByName(input.name())).thenReturn(List.of(existing));

        assertThatThrownBy(() -> useCase.run(input))
                .isInstanceOf(RestaurantAlreadyExistsException.class);

        verify(restaurantGateway, never()).save(any());
    }
}
