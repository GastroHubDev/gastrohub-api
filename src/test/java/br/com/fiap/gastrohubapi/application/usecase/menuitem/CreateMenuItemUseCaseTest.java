package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private CreateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateMenuItemUseCase(gateway);
    }

    @Test
    void shouldDelegateToGatewayAndReturnSavedItem() {
        UUID restaurantId = UUID.randomUUID();
        MenuItem input = new MenuItem(null, "Burger", "Juicy burger",
                new BigDecimal("25.90"), true, "/photos/burger.jpg", restaurantId);
        MenuItem saved = new MenuItem(UUID.randomUUID(), "Burger", "Juicy burger",
                new BigDecimal("25.90"), true, "/photos/burger.jpg", restaurantId);

        when(gateway.save(input)).thenReturn(saved);

        MenuItem result = useCase.execute(input);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Burger");
        verify(gateway).save(input);
    }
}