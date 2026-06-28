package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeMapperTest {

    @Test
    void toResponse_shouldMapAllFields() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        UserTypeResponse response = UserTypeMapper.toResponse(userType);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Client");
        assertThat(response.baseCategory()).isEqualTo(BaseCategory.CLIENT);
    }

    @Test
    void toResponseList_shouldMapAllItems() {
        List<UserType> userTypes = List.of(
                new UserType(1L, "Client", BaseCategory.CLIENT),
                new UserType(2L, "Owner", BaseCategory.OWNER)
        );

        List<UserTypeResponse> response = UserTypeMapper.toResponseList(userTypes);

        assertThat(response).hasSize(2);

        assertThat(response.get(0).id()).isEqualTo(1L);
        assertThat(response.get(0).name()).isEqualTo("Client");
        assertThat(response.get(0).baseCategory()).isEqualTo(BaseCategory.CLIENT);

        assertThat(response.get(1).id()).isEqualTo(2L);
        assertThat(response.get(1).name()).isEqualTo("Owner");
        assertThat(response.get(1).baseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void toResponseList_shouldReturnEmptyListWhenInputIsEmpty() {
        List<UserTypeResponse> response = UserTypeMapper.toResponseList(List.of());

        assertThat(response).isEmpty();
    }
}