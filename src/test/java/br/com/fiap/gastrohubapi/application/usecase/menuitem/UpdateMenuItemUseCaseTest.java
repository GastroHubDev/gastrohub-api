package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private UpdateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(gateway);
    }

    @Test
    void shouldUpdateAndReturnUpdatedItem() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        MenuItem updatedData = new MenuItem(null, "Burger XL", "Bigger burger",
                new BigDecimal("32.00"), true, "/photos/burger-xl.jpg", restaurantId);
        MenuItem expected = new MenuItem(id, "Burger XL", "Bigger burger",
                new BigDecimal("32.00"), true, "/photos/burger-xl.jpg", restaurantId);

        when(gateway.existsById(id)).thenReturn(true);
        when(gateway.update(any())).thenReturn(expected);

        MenuItem result = useCase.execute(id, updatedData);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Burger XL");
        verify(gateway).update(any(MenuItem.class));
    }

    @Test
    void shouldPassCorrectIdToUpdate() {
        UUID id = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        MenuItem updatedData = new MenuItem(null, "Burger XL", "Bigger burger",
                new BigDecimal("32.00"), true, null, restaurantId);
        MenuItem expected = new MenuItem(id, "Burger XL", "Bigger burger",
                new BigDecimal("32.00"), true, null, restaurantId);

        when(gateway.existsById(id)).thenReturn(true);
        when(gateway.update(any())).thenReturn(expected);

        useCase.execute(id, updatedData);

        verify(gateway).update(argThat(item -> id.equals(item.getId())));
    }

    @Test
    void shouldThrowWhenItemNotFound() {
        UUID id = UUID.randomUUID();
        MenuItem updatedData = new MenuItem(null, "X", "X",
                new BigDecimal("1.00"), false, null, UUID.randomUUID());

        when(gateway.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(id, updatedData))
                .isInstanceOf(MenuItemNotFoundException.class);
    }

    private static <T> T argThat(java.util.function.Predicate<T> predicate) {
        return org.mockito.ArgumentMatchers.argThat(predicate::test);
    }
}