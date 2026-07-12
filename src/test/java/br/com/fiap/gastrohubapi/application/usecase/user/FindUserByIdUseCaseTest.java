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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock
    private UserGateway userGateway;

    private FindUserByIdUseCase useCase;

    private UUID userId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        
        useCase = new FindUserByIdUseCase(userGateway); 
        userId = UUID.randomUUID();

        UserType userType = UserType.restore(TYPE_ID_1, "CLIENT", BaseCategory.CLIENT);
        expectedUser = User.create("Joao", "v@test.com", "123", userType);
    }

    @Test
    void shouldFindUserById() {
        when(userGateway.findById(userId)).thenReturn(Optional.of(expectedUser));
        
        User result = useCase.run(userId);

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> useCase.run(userId));
    }
}