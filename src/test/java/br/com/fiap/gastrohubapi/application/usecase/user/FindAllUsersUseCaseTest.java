package br.com.fiap.gastrohubapi.application.usecase.user;

import java.util.UUID;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FindAllUsersUseCaseTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock private UserGateway userGateway;
    private FindAllUsersUseCase useCase;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        useCase = new FindAllUsersUseCase(userGateway);
        UserType validUserType = UserType.restore(TYPE_ID_1, "CLIENT", BaseCategory.CLIENT);
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