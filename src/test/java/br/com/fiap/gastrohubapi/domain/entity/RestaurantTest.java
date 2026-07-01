package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestaurantTest {

    @Test
    void create_withValidData_shouldCreateRestaurantWithNullId() {
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.create("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);

        assertThat(restaurant.getId()).isNull();
        assertThat(restaurant.getName()).isEqualTo("Restaurante Bom");
        assertThat(restaurant.getAddress()).isEqualTo("Rua 1, 123");
        assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(restaurant.getOpeningHours()).isEqualTo("10:00-22:00");
        assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void create_shouldTrimNameAddressAndOpeningHours() {
        Restaurant restaurant = Restaurant.create("  Restaurante Bom  ", "  Rua 1, 123  ",
                KitchenType.ITALIAN, "  10:00-22:00  ", UUID.randomUUID());

        assertThat(restaurant.getName()).isEqualTo("Restaurante Bom");
        assertThat(restaurant.getAddress()).isEqualTo("Rua 1, 123");
        assertThat(restaurant.getOpeningHours()).isEqualTo("10:00-22:00");
    }

    @Test
    void create_withNullName_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create(null, "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name");
    }

    @Test
    void create_withBlankName_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create("   ", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name");
    }

    @Test
    void create_withNullAddress_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create("Restaurante Bom", null,
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Address");
    }

    @Test
    void create_withNullKitchenType_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create("Restaurante Bom", "Rua 1, 123",
                null, "10:00-22:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("KitchenType");
    }

    @Test
    void create_withNullOpeningHours_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, null, UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("OpeningHours");
    }

    @Test
    void create_withNullRestaurantOwnerId_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.create("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RestaurantOwnerId");
    }

    @Test
    void restore_withValidData_shouldRestoreRestaurantWithGivenId() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.restore(id, "Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);

        assertThat(restaurant.getId()).isEqualTo(id);
        assertThat(restaurant.getName()).isEqualTo("Restaurante Bom");
        assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void restore_withNullId_shouldThrow() {
        assertThatThrownBy(() -> Restaurant.restore(null, "Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id");
    }

    @Test
    void update_withValidData_shouldUpdateAllFields() {
        Restaurant restaurant = Restaurant.create("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID());
        UUID newOwnerId = UUID.randomUUID();

        restaurant.update("Sushi Bom", "Rua 2, 456",
                KitchenType.BRAZILIAN, "11:00-23:00", newOwnerId);

        assertThat(restaurant.getName()).isEqualTo("Sushi Bom");
        assertThat(restaurant.getAddress()).isEqualTo("Rua 2, 456");
        assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.BRAZILIAN);
        assertThat(restaurant.getOpeningHours()).isEqualTo("11:00-23:00");
        assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(newOwnerId);
    }

    @Test
    void update_withInvalidName_shouldThrowAndKeepRestaurantUnchanged() {
        Restaurant restaurant = Restaurant.create("Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID());

        assertThatThrownBy(() -> restaurant.update(null, "Rua 2, 456",
                KitchenType.BRAZILIAN, "11:00-23:00", UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(restaurant.getName()).isEqualTo("Restaurante Bom");
    }
}
