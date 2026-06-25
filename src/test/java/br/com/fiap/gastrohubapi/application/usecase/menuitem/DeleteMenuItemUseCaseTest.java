package br.com.fiap.gastrohubapi.application.usecase.menuitem;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway gateway;

    private DeleteMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteMenuItemUseCase(gateway);
    }

    @Test
    void shouldCallDeleteWhenItemExists() {
        UUID id = UUID.randomUUID();
        when(gateway.existsById(id)).thenReturn(true);

        useCase.execute(id);

        verify(gateway).delete(id);
    }

    @Test
    void shouldThrowWhenItemNotFound() {
        UUID id = UUID.randomUUID();
        when(gateway.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(id))
                .isInstanceOf(MenuItemNotFoundException.class);
    }
}