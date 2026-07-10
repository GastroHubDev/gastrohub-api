package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeJpaEntityTest {

    @Test
    void fromDomainShouldCopyAllFields() {
        UserType userType = UserType.restore(1L, "Client", BaseCategory.CLIENT);

        UserTypeJpaEntity entity = UserTypeJpaEntity.fromDomain(userType);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Client");
        assertThat(entity.getBaseCategory()).isEqualTo(BaseCategory.CLIENT);
    }

    @Test
    void toDomainShouldRestoreUserType() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity(2L, "Owner", BaseCategory.OWNER);

        UserType userType = entity.toDomain();

        assertThat(userType.getId()).isEqualTo(2L);
        assertThat(userType.getName()).isEqualTo("Owner");
        assertThat(userType.getBaseCategory()).isEqualTo(BaseCategory.OWNER);
    }

    @Test
    void idOnlyConstructorShouldSetOnlyId() {
        UserTypeJpaEntity entity = new UserTypeJpaEntity(3L);

        assertThat(entity.getId()).isEqualTo(3L);
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
