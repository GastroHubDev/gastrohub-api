package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeJpaEntityTest {

    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TYPE_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID TYPE_ID_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void fromDomainShouldCopyAllFields() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        UserTypeJpaEntity entity = UserTypeJpaEntity.fromDomain(userType);

        assertThat(entity.getId()).isEqualTo(TYPE_ID_1);
        assertThat(entity.getName()).isEqualTo("Client");
        assertThat(entity.getBaseCategory()).isEqualTo(BaseCategory.CLIENT);
    }

    @Test
    void toDomainShouldRestoreUserType() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity(TYPE_ID_2, "Owner", BaseCategory.OWNER);

        UserType userType = entity.toDomain();

        assertThat(userType.getId()).isEqualTo(TYPE_ID_2);
        assertThat(userType.getName()).isEqualTo("Owner");
        assertThat(userType.getBaseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void idOnlyConstructorShouldSetOnlyId() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity(TYPE_ID_3);

        assertThat(entity.getId()).isEqualTo(TYPE_ID_3);
        assertThat(entity.getName()).isNull();
        assertThat(entity.getBaseCategory()).isNull();
    }

    @Test
    void noArgConstructorShouldCreateEmptyInstance() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getBaseCategory()).isNull();
    }
}
