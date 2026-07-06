package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByEmailUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindUserByEmailUseCase useCase;
    private UUID userId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByEmailUseCase(userGateway);
        userId = UUID.randomUUID();
        UserType userType = new UserType(1L, "CLIENT", BaseCategory.CLIENT);
        expectedUser = User.create("Joao", "v@test.com", "123", userType);
    }

    @Test
    void shouldFindUserById() {
        when(userGateway.findById(userId)).thenReturn(Optional.of(expectedUser));
        User result = useCase.execute(String.valueOf(userId));
        assertNotNull(result);
    }




    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> useCase.execute(String.valueOf(userId)));
    }
}