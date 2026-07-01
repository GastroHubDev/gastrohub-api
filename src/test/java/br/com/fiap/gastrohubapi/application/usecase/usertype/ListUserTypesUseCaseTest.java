package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUserTypesUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private ListUserTypesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListUserTypesUseCase(userTypeGateway);
    }

    @Test
    void shouldListUserTypes() {
        List<UserType> userTypes = List.of(
                new UserType(1L, "Client", BaseCategory.CLIENT),
                new UserType(2L, "Owner", BaseCategory.OWNER)
        );

        when(userTypeGateway.findAll()).thenReturn(userTypes);

        List<UserType> result = useCase.execute();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(userTypes);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUserTypes() {
        when(userTypeGateway.findAll()).thenReturn(List.of());

        List<UserType> result = useCase.execute();

        assertThat(result).isEmpty();
    }
}