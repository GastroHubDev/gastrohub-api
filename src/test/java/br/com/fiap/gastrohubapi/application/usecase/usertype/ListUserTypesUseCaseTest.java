package br.com.fiap.gastrohubapi.application.usecase.usertype;

import java.util.UUID;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
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
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TYPE_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");


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
                UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT),
                UserType.restore(TYPE_ID_2, "Owner", BaseCategory.OWNER)
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