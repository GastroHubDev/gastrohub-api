package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.application.usecase.user.input.UpdateUserDTO;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private UserTypeGateway userTypeGateway;

    private UpdateUserUseCase useCase;

    private static final String NEW_NAME = "Joao Atualizado";
    private static final String NEW_EMAIL = "Joao.novo@test.com";
    private static final String PASSWORD = "password123";
    private static final Long USER_TYPE_ID = 1L;

    private UUID userId;
    private UpdateUserDTO input;
    private User existingUser;
    private UserType userType;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserUseCase(userGateway, userTypeGateway);
        userId = UUID.randomUUID();
        userType = UserType.restore(USER_TYPE_ID, "CLIENT", BaseCategory.CLIENT);

        input = new UpdateUserDTO(userId, NEW_NAME, NEW_EMAIL, USER_TYPE_ID, PASSWORD);

        existingUser = User.create("Nome Antigo", "antigo@test.com", PASSWORD, userType);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userGateway.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());
        when(userTypeGateway.findById(USER_TYPE_ID)).thenReturn(Optional.of(userType));
        when(userGateway.update(any(User.class))).thenReturn(existingUser);

        User result = useCase.execute(input);

        assertNotNull(result);
        verify(userGateway, times(1)).update(existingUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.execute(input));
        verify(userGateway, never()).update(any());
    }

    @Test
    void shouldThrowExceptionWhenNewEmailAlreadyInUseByAnotherUser() {
        UserType userTypeLocal = UserType.restore(1L, "CLIENT", BaseCategory.CLIENT);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userGateway.findByEmail(NEW_EMAIL)).thenReturn(Optional.of(User.create("Outro", NEW_EMAIL, "123", userTypeLocal)));

        assertThrows(UserAlreadyExistsException.class, () -> useCase.execute(input));
        verify(userGateway, never()).update(any());
    }
}