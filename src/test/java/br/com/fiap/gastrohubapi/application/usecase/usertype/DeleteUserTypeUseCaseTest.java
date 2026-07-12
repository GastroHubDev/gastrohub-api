package br.com.fiap.gastrohubapi.application.usecase.usertype;

import java.util.UUID;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUseCaseTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock
    private UserTypeGateway userTypeGateway;

    private DeleteUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserTypeUseCase(userTypeGateway);
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(TYPE_ID_1)).thenReturn(Optional.of(userType));
        when(userTypeGateway.isInUse(TYPE_ID_1)).thenReturn(false);

        useCase.execute(TYPE_ID_1);

        verify(userTypeGateway).deleteById(TYPE_ID_1);
    }

    @Test
    void shouldThrowWhenUserTypeIsNotFound() {
        when(userTypeGateway.findById(TYPE_ID_1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(TYPE_ID_1))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessage("User type not found.");

        verify(userTypeGateway, never()).deleteById(TYPE_ID_1);
    }

    @Test
    void shouldThrowWhenUserTypeIsInUse() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(TYPE_ID_1)).thenReturn(Optional.of(userType));
        when(userTypeGateway.isInUse(TYPE_ID_1)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(TYPE_ID_1))
                .isInstanceOf(UserTypeInUseException.class)
                .hasMessage("Cannot delete user type because it is in use.");

        verify(userTypeGateway, never()).deleteById(TYPE_ID_1);
    }
}