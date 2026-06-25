package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMenuItemsByRestaurantUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private FindMenuItemsByRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemsByRestaurantUseCase(gateway);
    }

    @Test
    void shouldReturnItemsForGivenRestaurant() {
        UUID restaurantId = UUID.randomUUID();
        List<MenuItem> items = List.of(
                new MenuItem(UUID.randomUUID(), "Sushi", "Fresh",
                        new BigDecimal("38.00"), true, null, restaurantId)
        );

        when(gateway.findAllByRestaurantId(restaurantId)).thenReturn(items);

        List<MenuItem> result = useCase.execute(restaurantId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void shouldReturnEmptyWhenRestaurantHasNoItems() {
        UUID restaurantId = UUID.randomUUID();
        when(gateway.findAllByRestaurantId(restaurantId)).thenReturn(List.of());

        assertThat(useCase.execute(restaurantId)).isEmpty();
    }
}