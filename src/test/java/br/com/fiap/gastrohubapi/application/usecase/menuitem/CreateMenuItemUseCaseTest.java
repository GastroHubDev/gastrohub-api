package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private CreateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateMenuItemUseCase(gateway, restaurantGateway);
    }

    @Test
    void shouldDelegateToGatewayAndReturnSavedItem() {
        UUID restaurantId = UUID.randomUUID();
        MenuItem input = MenuItem.create("Burger", "Juicy burger",
                new BigDecimal("25.90"), true, "/photos/burger.jpg", restaurantId);
        MenuItem saved = MenuItem.restore(UUID.randomUUID(), "Burger", "Juicy burger",
                new BigDecimal("25.90"), true, "/photos/burger.jpg", restaurantId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(mock(br.com.fiap.gastrohubapi.domain.entity.Restaurant.class)));
        when(gateway.save(input)).thenReturn(saved);

        MenuItem result = useCase.execute(input);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Burger");
        verify(gateway).save(input);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        MenuItem input = MenuItem.create("Burger", "Juicy burger",
                new BigDecimal("25.90"), true, null, restaurantId);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(input))
                .isInstanceOf(RestaurantNotFoundByIdException.class);
    }
}
