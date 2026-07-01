package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private CreateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserTypeUseCase(userTypeGateway);
    }

    @Test
    void shouldCreateUserType() {
        UserType savedUserType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.existsByName("Client")).thenReturn(false);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedUserType);

        UserType result = useCase.execute("Client", BaseCategory.CLIENT);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Client");
        assertThat(result.getBaseCategory()).isEqualTo(BaseCategory.CLIENT);

        ArgumentCaptor<UserType> captor = ArgumentCaptor.forClass(UserType.class);
        verify(userTypeGateway).save(captor.capture());

        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getName()).isEqualTo("Client");
        assertThat(captor.getValue().getBaseCategory()).isEqualTo(BaseCategory.CLIENT);
    }

    @Test
    void shouldTrimNameBeforeCreatingUserType() {
        UserType savedUserType = new UserType(1L, "Client", BaseCategory.CLIENT);

        when(userTypeGateway.existsByName("Client")).thenReturn(false);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(savedUserType);

        useCase.execute("  Client  ", BaseCategory.CLIENT);

        verify(userTypeGateway).existsByName("Client");
    }

    @Test
    void shouldThrowWhenNameAlreadyExists() {
        when(userTypeGateway.existsByName("Client")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("Client", BaseCategory.CLIENT))
                .isInstanceOf(DuplicateUserTypeNameException.class)
                .hasMessage("User type name already exists.");

        verify(userTypeGateway, never()).save(any(UserType.class));
    }
}