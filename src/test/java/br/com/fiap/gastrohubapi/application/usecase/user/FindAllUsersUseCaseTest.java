package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FindAllUsersUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindAllUsersUseCase useCase;
    private UUID userId;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        useCase = new FindAllUsersUseCase(userGateway);
        userId = UUID.randomUUID();
        UserType validUserType = new UserType(1L, "CLIENT", BaseCategory.CLIENT);
        expectedUser = User.create("Joao", "v@test.com", "123", validUserType);    }

    @Test
    void shouldReturnAllUsers() {
        when(userGateway.findAll()).thenReturn(List.of(expectedUser));

        List<User> result = useCase.execute();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}