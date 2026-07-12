package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantMapperTest {

    @Test
    void toResponse_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.restore(id, "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", ownerId);

        RestaurantResponse result = RestaurantMapper.toResponse(restaurant);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Cantina da Nona");
        assertThat(result.address()).isEqualTo("Rua das Flores, 123");
        assertThat(result.kitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(result.openingHours()).isEqualTo("08:00-22:00");
        assertThat(result.restaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void toResponseList_shouldMapEveryElementPreservingOrder() {
        Restaurant first = Restaurant.restore(UUID.randomUUID(), "Cantina da Nona", "Rua das Flores, 123",
                KitchenType.ITALIAN, "08:00-22:00", UUID.randomUUID());
        Restaurant second = Restaurant.restore(UUID.randomUUID(), "Sushi House", "Av. Central, 99",
                KitchenType.JAPANESE, "11:00-23:00", UUID.randomUUID());

        List<RestaurantResponse> result = RestaurantMapper.toResponseList(List.of(first, second));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(first.getId());
        assertThat(result.get(0).name()).isEqualTo("Cantina da Nona");
        assertThat(result.get(0).kitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(result.get(1).id()).isEqualTo(second.getId());
        assertThat(result.get(1).name()).isEqualTo("Sushi House");
        assertThat(result.get(1).kitchenType()).isEqualTo(KitchenType.JAPANESE);
    }

    @Test
    void toResponseList_withEmptyList_shouldReturnEmptyList() {
        List<RestaurantResponse> result = RestaurantMapper.toResponseList(List.of());

        assertThat(result).isEmpty();
    }
}
