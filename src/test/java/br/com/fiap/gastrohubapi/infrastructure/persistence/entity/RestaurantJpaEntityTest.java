package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantJpaEntityTest {

    @Test
    void fromDomainShouldCopyAllFields() {
        UUID ownerId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.create("Cantina da Nonna", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", ownerId);

        RestaurantJpaEntity entity = RestaurantJpaEntity.fromDomain(restaurant);

        assertThat(entity.getName()).isEqualTo("Cantina da Nonna");
        assertThat(entity.getAddress()).isEqualTo("Rua das Flores, 123");
        assertThat(entity.getKitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(entity.getOpeningHours()).isEqualTo("08:00-22:00");
        assertThat(entity.getRestaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void toDomainShouldRestoreRestaurant() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        RestaurantJpaEntity entity = new RestaurantJpaEntity(id, "Sushi House", "Av. Central, 456",
                KitchenType.JAPANESE, "11:00-23:00", ownerId);

        Restaurant restaurant = entity.toDomain();

        assertThat(restaurant.getId()).isEqualTo(id);
        assertThat(restaurant.getName()).isEqualTo("Sushi House");
        assertThat(restaurant.getAddress()).isEqualTo("Av. Central, 456");
        assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.JAPANESE);
        assertThat(restaurant.getOpeningHours()).isEqualTo("11:00-23:00");
        assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void settersShouldUpdateFields() {
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        entity.setId(id);
        entity.setName("Cantina Nova");
        entity.setAddress("Nova Rua, 789");
        entity.setKitchenType(KitchenType.BRAZILIAN);
        entity.setOpeningHours("09:00-21:00");
        entity.setRestaurantOwnerId(ownerId);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Cantina Nova");
        assertThat(entity.getAddress()).isEqualTo("Nova Rua, 789");
        assertThat(entity.getKitchenType()).isEqualTo(KitchenType.BRAZILIAN);
        assertThat(entity.getOpeningHours()).isEqualTo("09:00-21:00");
        assertThat(entity.getRestaurantOwnerId()).isEqualTo(ownerId);
    }
}
