package br.com.fiap.gastrohubapi.application.usecase.user;

import java.util.UUID;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock
    private UserGateway userGateway;

    @Mock
    private UserTypeGateway userTypeGateway;

    private CreateUserUseCase useCase;

    private static final String NAME = "Joao";
    private static final String EMAIL = "Joao@test.com";
    private static final String PASSWORD = "password123";
    private static final UUID USER_TYPE_ID = TYPE_ID_1;

    private NewUserDTO input;
    private User expectedUser;
    private UserType userType;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userGateway, userTypeGateway);
        userType = UserType.restore(USER_TYPE_ID, "CLIENT", BaseCategory.CLIENT);

        input = new NewUserDTO(NAME, EMAIL, USER_TYPE_ID, PASSWORD);
        expectedUser = User.create(NAME, EMAIL, PASSWORD, userType);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userGateway.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userTypeGateway.findById(USER_TYPE_ID)).thenReturn(Optional.of(userType));
        when(userGateway.save(any(User.class))).thenReturn(expectedUser);

        User result = useCase.run(input);

        assertNotNull(result);
        assertEquals(NAME, result.getName());
        assertEquals(EMAIL, result.getEmail());

        verify(userGateway, times(1)).findByEmail(EMAIL);
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userGateway.findByEmail(EMAIL)).thenReturn(Optional.of(expectedUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> useCase.run(input));

        assertEquals("User with email " + EMAIL + " already exists.", exception.getMessage());
        verify(userGateway, never()).save(any(User.class));
    }
}