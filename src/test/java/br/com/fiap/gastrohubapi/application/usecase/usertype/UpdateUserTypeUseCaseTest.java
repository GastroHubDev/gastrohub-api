package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private UpdateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserTypeUseCase(userTypeGateway);
    }

    @Test
    void shouldUpdateUserType() {
        UserType existingUserType = new UserType(1L, "Client", BaseCategory.CLIENT);
        UserType savedUserType = new UserType(1L, "Owner", BaseCategory.OWNER);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existingUserType));
        when(userTypeGateway.existsByNameAndIdNot("Owner", 1L)).thenReturn(false);
        when(userTypeGateway.isInUse(1L)).thenReturn(false);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedUserType);

        UserType result = useCase.execute(1L, "Owner", BaseCategory.OWNER);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Owner");
        assertThat(result.getBaseCategory()).isEqualTo(BaseCategory.OWNER);

        ArgumentCaptor<UserType> captor = ArgumentCaptor.forClass(UserType.class);
        verify(userTypeGateway).save(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(1L);
        assertThat(captor.getValue().getName()).isEqualTo("Owner");
        assertThat(captor.getValue().getBaseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void shouldThrowWhenUserTypeIsNotFound() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L, "Owner", BaseCategory.OWNER))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessage("User type not found.");

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void shouldThrowWhenNameAlreadyExistsForAnotherUserType() {
        UserType existingUserType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existingUserType));
        when(userTypeGateway.existsByNameAndIdNot("Owner", 1L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(1L, "Owner", BaseCategory.CLIENT))
                .isInstanceOf(DuplicateUserTypeNameException.class)
                .hasMessage("User type name already exists.");

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void shouldThrowWhenChangingBaseCategoryAndUserTypeIsInUse() {
        UserType existingUserType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(existingUserType));
        when(userTypeGateway.existsByNameAndIdNot("Client", 1L)).thenReturn(false);
        when(userTypeGateway.isInUse(1L)).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(1L, "Client", BaseCategory.OWNER))
                .isInstanceOf(UserTypeInUseException.class)
                .hasMessage("Cannot change base category because user type is in use.");

        verify(userTypeGateway, never()).save(any(UserType.class));
    }
}