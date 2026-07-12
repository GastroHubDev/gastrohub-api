package br.com.fiap.gastrohubapi.application.usecase.usertype;

import java.util.UUID;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
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
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Mock
    private UserTypeGateway userTypeGateway;

    private FindUserTypeByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserTypeByIdUseCase(userTypeGateway);
    }

    @Test
    void shouldFindUserTypeById() {
        UserType userType = UserType.restore(TYPE_ID_1, "Owner", BaseCategory.OWNER);

        when(userTypeGateway.findById(TYPE_ID_1)).thenReturn(Optional.of(userType));

        UserType result = useCase.execute(TYPE_ID_1);

        assertThat(result.getId()).isEqualTo(TYPE_ID_1);
        assertThat(result.getName()).isEqualTo("Owner");
        assertThat(result.getBaseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void shouldThrowWhenUserTypeIsNotFound() {
        when(userTypeGateway.findById(TYPE_ID_1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(TYPE_ID_1))
                .isInstanceOf(UserTypeNotFoundException.class)
                .hasMessage("User type not found.");
    }
}