package br.com.fiap.gastrohubapi.presentation.mapper;

import java.util.UUID;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeMapperTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TYPE_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");


    @Test
    void toResponse_shouldMapAllFields() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        UserTypeResponse response = UserTypeMapper.toResponse(userType);

        assertThat(response.id()).isEqualTo(TYPE_ID_1);
        assertThat(response.name()).isEqualTo("Client");
        assertThat(response.baseCategory()).isEqualTo(BaseCategory.CLIENT);
    }

    @Test
    void toResponseList_shouldMapAllItems() {
        List<UserType> userTypes = List.of(
                UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT),
                UserType.restore(TYPE_ID_2, "Owner", BaseCategory.OWNER)
        );

        List<UserTypeResponse> response = UserTypeMapper.toResponseList(userTypes);

        assertThat(response).hasSize(2);

        assertThat(response.get(0).id()).isEqualTo(TYPE_ID_1);
        assertThat(response.get(0).name()).isEqualTo("Client");
        assertThat(response.get(0).baseCategory()).isEqualTo(BaseCategory.CLIENT);

        assertThat(response.get(1).id()).isEqualTo(TYPE_ID_2);
        assertThat(response.get(1).name()).isEqualTo("Owner");
        assertThat(response.get(1).baseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void toResponseList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<UserTypeResponse> response = UserTypeMapper.toResponseList(List.of());

        assertThat(response).isEmpty();
    }
}