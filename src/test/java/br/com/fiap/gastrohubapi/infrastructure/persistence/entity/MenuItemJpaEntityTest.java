package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemJpaEntityTest {

    @Test
    void settersShouldUpdateFields() {
        MenuItemJpaEntity entity = new MenuItemJpaEntity();
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        entity.setId(id);
        entity.setName("Pizza");
        entity.setDescription("Pizza margherita");
        entity.setPrice(new BigDecimal("39.90"));
        entity.setOnlyInRestaurant(true);
        entity.setPhotoPath("/photos/pizza.jpg");
        entity.setRestaurantId(restaurantId);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Pizza");
        assertThat(entity.getDescription()).isEqualTo("Pizza margherita");
        assertThat(entity.getPrice()).isEqualByComparingTo("39.90");
        assertThat(entity.isOnlyInRestaurant()).isTrue();
        assertThat(entity.getPhotoPath()).isEqualTo("/photos/pizza.jpg");
        assertThat(entity.getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void allArgsConstructorShouldSetAllFields() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        MenuItemJpaEntity entity = new MenuItemJpaEntity(id, "Burger", "Burger artesanal",
                new BigDecimal("25.00"), false, "/photos/burger.jpg", restaurantId);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Burger");
        assertThat(entity.isOnlyInRestaurant()).isFalse();
        assertThat(entity.getRestaurantId()).isEqualTo(restaurantId);
    }
}
