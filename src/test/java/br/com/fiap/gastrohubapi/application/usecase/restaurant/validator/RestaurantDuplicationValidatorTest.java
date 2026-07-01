package br.com.fiap.gastrohubapi.application.usecase.restaurant.validator;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantDuplicationValidatorTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private RestaurantDuplicationValidator validator;

    private UUID ownerId;

    @BeforeEach
    void setUp() {
        validator = RestaurantDuplicationValidator.create(restaurantGateway);
        ownerId = UUID.randomUUID();
    }

    @Test
    void shouldNotThrowWhenNoRestaurantWithSameNameExists() {
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of());

        assertThatCode(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, null))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWhenSameNameOwnerAddressAndKitchenTypeExist() {
        Restaurant existing = Restaurant.create("Restaurante Bom", "Rua 1", KitchenType.ITALIAN,
                "10:00-22:00", ownerId);
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of(existing));

        assertThatThrownBy(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, null))
                .isInstanceOf(RestaurantAlreadyExistsException.class);
    }

    @Test
    void shouldNotThrowWhenAddressDiffers() {
        Restaurant existing = Restaurant.create("Restaurante Bom", "Rua 2", KitchenType.ITALIAN,
                "10:00-22:00", ownerId);
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of(existing));

        assertThatCode(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, null))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldNotThrowWhenOwnerDiffers() {
        Restaurant existing = Restaurant.create("Restaurante Bom", "Rua 1", KitchenType.ITALIAN,
                "10:00-22:00", UUID.randomUUID());
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of(existing));

        assertThatCode(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, null))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldNotThrowWhenKitchenTypeDiffers() {
        Restaurant existing = Restaurant.create("Restaurante Bom", "Rua 1", KitchenType.JAPANESE,
                "10:00-22:00", ownerId);
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of(existing));

        assertThatCode(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, null))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldIgnoreRestaurantWithGivenIdToIgnore() {
        Restaurant existing = Restaurant.restore(UUID.randomUUID(), "Restaurante Bom", "Rua 1",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);
        when(restaurantGateway.findByName("Restaurante Bom")).thenReturn(List.of(existing));

        assertThatCode(() -> validator.assertNoDuplicate("Restaurante Bom", ownerId, "Rua 1",
                KitchenType.ITALIAN, existing.getId()))
                .doesNotThrowAnyException();
    }
}
