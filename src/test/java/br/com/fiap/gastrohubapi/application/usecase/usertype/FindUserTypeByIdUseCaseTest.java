package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserTypeByIdUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private FindUserTypeByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserTypeByIdUseCase(userTypeGateway);
    }

    @Test
    void shouldFindUserTypeById() {
        UserType userType = new UserType(1L, "Owner", BaseCategory.OWNER);

        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));

        UserType result = useCase.execute(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Owner");
        assertThat(result.getBaseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void shouldThrowWhenUserTypeIsNotFound() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessage("User type not found.");
    }
}