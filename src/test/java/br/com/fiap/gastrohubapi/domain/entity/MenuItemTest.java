package br.com.fiap.gastrohubapi.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuItemTest {

    private static final UUID RESTAURANT_ID = UUID.randomUUID();

    @Test
    void createShouldBuildItemWithoutIdWhenDataIsValid() {
        MenuItem item = MenuItem.create("Pizza", "Margherita", new BigDecimal("39.90"),
                true, "/photos/pizza.jpg", RESTAURANT_ID);

        assertThat(item.getId()).isNull();
        assertThat(item.getName()).isEqualTo("Pizza");
        assertThat(item.getDescription()).isEqualTo("Margherita");
        assertThat(item.getPrice()).isEqualByComparingTo("39.90");
        assertThat(item.isOnlyInRestaurant()).isTrue();
        assertThat(item.getPhotoPath()).isEqualTo("/photos/pizza.jpg");
        assertThat(item.getRestaurantId()).isEqualTo(RESTAURANT_ID);
    }

    @Test
    void restoreShouldKeepProvidedId() {
        UUID id = UUID.randomUUID();
        MenuItem item = MenuItem.restore(id, "Pizza", "Margherita", new BigDecimal("39.90"),
                false, null, RESTAURANT_ID);

        assertThat(item.getId()).isEqualTo(id);
        assertThat(item.isOnlyInRestaurant()).isFalse();
        assertThat(item.getPhotoPath()).isNull();
    }

    @Test
    void createShouldRejectNullName() {
        assertThatThrownBy(() -> MenuItem.create(null, "desc", BigDecimal.TEN, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    @Test
    void createShouldRejectBlankName() {
        assertThatThrownBy(() -> MenuItem.create("   ", "desc", BigDecimal.TEN, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    @Test
    void createShouldRejectNullDescription() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", null, BigDecimal.TEN, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("description");
    }

    @Test
    void createShouldRejectBlankDescription() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", "  ", BigDecimal.TEN, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("description");
    }

    @Test
    void createShouldRejectNullPrice() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", "desc", null, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("price");
    }

    @Test
    void createShouldRejectZeroPrice() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", "desc", BigDecimal.ZERO, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("price");
    }

    @Test
    void createShouldRejectNegativePrice() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", "desc", new BigDecimal("-1"), true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("price");
    }

    @Test
    void createShouldRejectNullRestaurantId() {
        assertThatThrownBy(() -> MenuItem.create("Pizza", "desc", BigDecimal.TEN, true, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("restaurantId");
    }

    @Test
    void restoreShouldRejectInvalidData() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> MenuItem.restore(id, "", "desc", BigDecimal.TEN, true, null, RESTAURANT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }
}
