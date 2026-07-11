package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserJpaEntityTest {

    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);
    private final UserTypeJpaEntity userTypeJpaEntity = UserTypeJpaEntity.fromDomain(userType);

    @Test
    void fromDomainShouldCopyAllFields() {
        UUID id = UUID.randomUUID();
        User user = User.restore(id, "Ana Lima", "ana@email.com", "senha123", userType);

        UserJpaEntity entity = UserJpaEntity.fromDomain(user);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Ana Lima");
        assertThat(entity.getEmail()).isEqualTo("ana@email.com");
        assertThat(entity.getPassword()).isEqualTo("senha123");
        assertThat(entity.getUserType()).isNotNull();
    }

    @Test
    void toDomainShouldRestoreUser() {
        UUID id = UUID.randomUUID();
        UserJpaEntity entity = new UserJpaEntity(id, "Bruno Costa", "bruno@email.com", "senha456", userTypeJpaEntity);

        User user = entity.toDomain();

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("Bruno Costa");
        assertThat(user.getEmail()).isEqualTo("bruno@email.com");
        assertThat(user.getPassword()).isEqualTo("senha456");
        assertThat(user.getUserType().getName()).isEqualTo("Client");
    }

    @Test
    void noArgConstructorShouldCreateEmptyInstance() {
        UserJpaEntity entity = new UserJpaEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
    }

    @Test
    void isNewShouldBeTrueByDefaultAndFalseAfterMarkNotNew() {
        UserJpaEntity entity = new UserJpaEntity(UUID.randomUUID(), "Ana", "ana@email.com", "senha123", userTypeJpaEntity);

        assertThat(entity.isNew()).isTrue();

        entity.markNotNew();

        assertThat(entity.isNew()).isFalse();
    }
}
