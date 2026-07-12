package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserJpaRepository;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserGatewayImplTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private UserTypeJpaRepository userTypeRepository;

    private UserGatewayImpl gateway;

    private UserType clientType;

    @BeforeEach
    void setUp() {
        gateway = new UserGatewayImpl(userRepository);

        UserTypeJpaEntity persistedType = userTypeRepository.save(
                new UserTypeJpaEntity(null, "Client", BaseCategory.CLIENT));
        clientType = UserType.restore(persistedType.getId(), persistedType.getName(), persistedType.getBaseCategory());
    }

    private User newUser(String name, String email) {
        return User.create(name, email, "senha123", clientType);
    }

    @Test
    void saveShouldPersistAndAssignId() {
        User saved = gateway.save(newUser("Ana Lima", "ana@email.com"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Ana Lima");
        assertThat(userRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByIdShouldReturnSavedUser() {
        User saved = gateway.save(newUser("Bruno Costa", "bruno@email.com"));

        assertThat(gateway.findById(saved.getId()))
                .isPresent()
                .get()
                .satisfies(user -> {
                    assertThat(user.getName()).isEqualTo("Bruno Costa");
                    assertThat(user.getEmail()).isEqualTo("bruno@email.com");
                    assertThat(user.getUserType().getName()).isEqualTo("Client");
                });
    }

    @Test
    void findByIdShouldReturnEmptyWhenAbsent() {
        assertThat(gateway.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByNameShouldFilterByExactName() {
        gateway.save(newUser("Ana Lima", "ana@email.com"));
        gateway.save(newUser("Bruno Costa", "bruno@email.com"));

        List<User> found = gateway.findByName("Ana Lima");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getEmail()).isEqualTo("ana@email.com");
    }

    @Test
    void findByNameShouldMatchPartialName() {
        gateway.save(newUser("Ana Lima", "ana@email.com"));
        gateway.save(newUser("Bruno Costa", "bruno@email.com"));

        List<User> found = gateway.findByName("Ana");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getEmail()).isEqualTo("ana@email.com");
    }

    @Test
    void findByNameShouldBeCaseInsensitive() {
        gateway.save(newUser("Ana Lima", "ana@email.com"));

        List<User> found = gateway.findByName("ana lima");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getEmail()).isEqualTo("ana@email.com");
    }

    @Test
    void findByEmailShouldReturnMatchingUser() {
        gateway.save(newUser("Ana Lima", "ana@email.com"));

        assertThat(gateway.findByEmail("ana@email.com"))
                .isPresent()
                .get()
                .satisfies(user -> assertThat(user.getName()).isEqualTo("Ana Lima"));
    }

    @Test
    void findByEmailShouldReturnEmptyWhenAbsent() {
        assertThat(gateway.findByEmail("naoexiste@email.com")).isEmpty();
    }

    @Test
    void findAllShouldReturnEveryUser() {
        gateway.save(newUser("Ana Lima", "ana@email.com"));
        gateway.save(newUser("Bruno Costa", "bruno@email.com"));

        assertThat(gateway.findAll()).hasSize(2);
    }

    @Test
    void updateShouldPersistChanges() {
        User saved = gateway.save(newUser("Ana Lima", "ana@email.com"));

        saved.update("Ana Lima Souza", "ana.souza@email.com", "novaSenha456", clientType);
        User updated = gateway.update(saved);

        assertThat(updated.getName()).isEqualTo("Ana Lima Souza");
        assertThat(gateway.findById(saved.getId()))
                .get()
                .satisfies(user -> {
                    assertThat(user.getName()).isEqualTo("Ana Lima Souza");
                    assertThat(user.getEmail()).isEqualTo("ana.souza@email.com");
                });
    }

    @Test
    void deleteShouldRemoveUser() {
        User saved = gateway.save(newUser("Ana Lima", "ana@email.com"));
        userRepository.flush();

        gateway.delete(saved.getId());

        assertThat(gateway.findById(saved.getId())).isEmpty();
    }
}
