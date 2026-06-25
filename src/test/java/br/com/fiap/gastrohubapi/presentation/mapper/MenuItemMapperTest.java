package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.MenuItemResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemMapperTest {

    @Test
    void toDomain_fromCreateRequest_shouldMapAllFieldsAndSetIdNull() {
        UUID restaurantId = UUID.randomUUID();
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Sushi", "Fresh salmon", new BigDecimal("38.00"),
                true, "/photos/sushi.jpg", restaurantId);

        MenuItem result = MenuItemMapper.toDomain(request);

        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Sushi");
        assertThat(result.getDescription()).isEqualTo("Fresh salmon");
        assertThat(result.getPrice()).isEqualByComparingTo("38.00");
        assertThat(result.isOnlyInRestaurant()).isTrue();
        assertThat(result.getPhotoPath()).isEqualTo("/photos/sushi.jpg");
        assertThat(result.getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void toDomain_fromUpdateRequest_shouldMapAllFieldsAndSetIdNull() {
        UUID restaurantId = UUID.randomUUID();
        UpdateMenuItemRequest request = new UpdateMenuItemRequest(
                "Sushi EX", "Updated description", new BigDecimal("42.00"),
                false, null, restaurantId);

        MenuItem result = MenuItemMapper.toDomain(request);

        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Sushi EX");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getPrice()).isEqualByComparingTo("42.00");
        assertThat(result.isOnlyInRestaurant()).isFalse();
        assertThat(result.getPhotoPath()).isNull();
        assertThat(result.getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void toResponse_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        MenuItem item = new MenuItem(id, "Sushi", "Fresh salmon",
                new BigDecimal("38.00"), true, "/photos/sushi.jpg", restaurantId);

        MenuItemResponse result = MenuItemMapper.toResponse(item);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Sushi");
        assertThat(result.description()).isEqualTo("Fresh salmon");
        assertThat(result.price()).isEqualByComparingTo("38.00");
        assertThat(result.onlyInRestaurant()).isTrue();
        assertThat(result.photoPath()).isEqualTo("/photos/sushi.jpg");
        assertThat(result.restaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void toResponse_withNullPhotoPath_shouldMapNullPhotoPath() {
        UUID id = UUID.randomUUID();
        MenuItem item = new MenuItem(id, "Burger", "desc",
                new BigDecimal("25.00"), false, null, UUID.randomUUID());

        MenuItemResponse result = MenuItemMapper.toResponse(item);

        assertThat(result.photoPath()).isNull();
    }
}