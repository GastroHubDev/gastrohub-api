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
class FindAllMenuItemsUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private FindAllMenuItemsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindAllMenuItemsUseCase(gateway);
    }

    @Test
    void shouldReturnAllItems() {
        List<MenuItem> items = List.of(
                MenuItem.restore(UUID.randomUUID(), "Burger", "desc",
                        new BigDecimal("25.00"), true, null, UUID.randomUUID()),
                MenuItem.restore(UUID.randomUUID(), "Pizza", "desc",
                        new BigDecimal("40.00"), false, null, UUID.randomUUID())
        );

        when(gateway.findAll()).thenReturn(items);

        List<MenuItem> result = useCase.execute();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(items);
    }

    @Test
    void shouldReturnEmptyListWhenNoItems() {
        when(gateway.findAll()).thenReturn(List.of());

        assertThat(useCase.execute()).isEmpty();
    }
}