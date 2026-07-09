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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByEmailUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindUserByEmailUseCase useCase;
    private User expectedUser;

    private static final String EMAIL = "v@test.com";

    @BeforeEach
    void setUp() {
        useCase = new FindUserByEmailUseCase(userGateway);
        UserType userType = UserType.restore(1L, "CLIENT", BaseCategory.CLIENT);
        expectedUser = User.create("Joao", EMAIL, "123", userType);
    }

    @Test
    void shouldFindUserByEmail() {
        when(userGateway.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));

        User result = useCase.execute(EMAIL);

        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> useCase.execute(EMAIL));
    }
}