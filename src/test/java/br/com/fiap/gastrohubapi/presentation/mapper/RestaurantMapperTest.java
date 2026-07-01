package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.RestaurantResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantMapperTest {

    @Test
    void toInput_fromCreateRequest_shouldMapAllFields() {
        UUID ownerId = UUID.randomUUID();
        CreateRestaurantRequest request = new CreateRestaurantRequest("Restaurante Bom",
                "Rua 1, 123", KitchenType.ITALIAN, "10:00-22:00", ownerId);

        NewRestaurantInput result = RestaurantMapper.toInput(request);

        assertThat(result.name()).isEqualTo("Restaurante Bom");
        assertThat(result.address()).isEqualTo("Rua 1, 123");
        assertThat(result.kitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(result.openingHours()).isEqualTo("10:00-22:00");
        assertThat(result.restaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void toInput_fromUpdateRequest_shouldMapAllFields() {
        UUID ownerId = UUID.randomUUID();
        UpdateRestaurantRequest request = new UpdateRestaurantRequest("Sushi Bom",
                "Rua 2, 456", KitchenType.BRAZILIAN, "11:00-23:00", ownerId);

        UpdateRestaurantInput result = RestaurantMapper.toInput(request);

        assertThat(result.name()).isEqualTo("Sushi Bom");
        assertThat(result.address()).isEqualTo("Rua 2, 456");
        assertThat(result.kitchenType()).isEqualTo(KitchenType.BRAZILIAN);
        assertThat(result.openingHours()).isEqualTo("11:00-23:00");
        assertThat(result.restaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void toResponse_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Restaurant restaurant = Restaurant.restore(id, "Restaurante Bom", "Rua 1, 123",
                KitchenType.ITALIAN, "10:00-22:00", ownerId);

        RestaurantResponse result = RestaurantMapper.toResponse(restaurant);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Restaurante Bom");
        assertThat(result.address()).isEqualTo("Rua 1, 123");
        assertThat(result.kitchenType()).isEqualTo(KitchenType.ITALIAN);
        assertThat(result.openingHours()).isEqualTo("10:00-22:00");
        assertThat(result.restaurantOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void toResponseList_shouldMapAllRestaurantsInOrder() {
        Restaurant r1 = Restaurant.restore(UUID.randomUUID(), "Restaurante Bom", "Rua 1",
                KitchenType.ITALIAN, "10:00-22:00", UUID.randomUUID());
        Restaurant r2 = Restaurant.restore(UUID.randomUUID(), "Pizzaria D'boa", "Rua 2",
                KitchenType.JAPANESE, "11:00-23:00", UUID.randomUUID());

        List<RestaurantResponse> result = RestaurantMapper.toResponseList(List.of(r1, r2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Restaurante Bom");
        assertThat(result.get(1).name()).isEqualTo("Pizzaria D'boa");
    }

    @Test
    void toResponseList_withEmptyList_shouldReturnEmptyList() {
        List<RestaurantResponse> result = RestaurantMapper.toResponseList(List.of());

        assertThat(result).isEmpty();
    }
}
