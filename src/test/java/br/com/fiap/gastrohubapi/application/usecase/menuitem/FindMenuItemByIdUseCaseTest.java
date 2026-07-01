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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private FindMenuItemByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindMenuItemByIdUseCase(gateway);
    }

    @Test
    void shouldReturnItemWhenFound() {
        UUID id = UUID.randomUUID();
        MenuItem item = MenuItem.restore(id, "Pizza", "Margherita",
                new BigDecimal("45.00"), false, null, UUID.randomUUID());

        when(gateway.findById(id)).thenReturn(Optional.of(item));

        MenuItem result = useCase.execute(id);

        assertThat(result).isEqualTo(item);
    }

    @Test
    void shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(MenuItemNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}