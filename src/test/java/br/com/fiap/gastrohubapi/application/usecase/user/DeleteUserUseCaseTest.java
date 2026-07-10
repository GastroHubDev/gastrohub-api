package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock
    private UserGateway userGateway;

    private DeleteUserUseCase useCase;

    private UUID userId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserUseCase(userGateway);
        userId = UUID.randomUUID();
        UserType userType = UserType.restore(TYPE_ID_1, "CLIENT", BaseCategory.CLIENT);

        existingUser = User.restore(userId, "Joao", "Joao@test.com", "123", userType);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userGateway).delete(userId);

        useCase.execute(userId);

        verify(userGateway, times(1)).delete(userId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(userId));
        verify(userGateway, never()).delete(any());
    }
}