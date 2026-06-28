package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
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

    @Mock
    private UserTypeGateway userTypeGateway;

    private DeleteUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserTypeUseCase(userTypeGateway);
    }

    @Test
    void shouldDeleteUserType() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));
        when(userTypeGateway.isInUse(1L)).thenReturn(false);

        useCase.execute(1L);

        verify(userTypeGateway).deleteById(1L);
    }

    @Test
    void shouldThrowWhenUserTypeIsNotFound() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessage("User type not found.");

        verify(userTypeGateway, never()).deleteById(1L);
    }

    @Test
    void shouldThrowWhenUserTypeIsInUse() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));
        when(userTypeGateway.isInUse(1L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(UserTypeInUseException.class)
                .hasMessage("Cannot delete user type because it is in use.");

        verify(userTypeGateway, never()).deleteById(1L);
    }
}