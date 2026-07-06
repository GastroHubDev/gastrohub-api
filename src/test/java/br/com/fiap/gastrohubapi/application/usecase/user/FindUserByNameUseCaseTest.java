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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FindUserByNameUseCaseTest {

    @Mock private UserGateway userGateway;
    private FindUserByNameUseCase useCase;
    private static final String SEARCH_NAME = "Joao";

    @BeforeEach
    void setUp() {
        useCase = new FindUserByNameUseCase(userGateway);
    }

    @Test
    void shouldReturnUserListByName() {

        UserType userType = new UserType(1L, "CLIENT", BaseCategory.CLIENT);

        User user = User.create(SEARCH_NAME, "v@test.com", "123", userType);
        when(userGateway.findByName(SEARCH_NAME)).thenReturn(List.of(user));

        List<User> result = useCase.run(SEARCH_NAME);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}