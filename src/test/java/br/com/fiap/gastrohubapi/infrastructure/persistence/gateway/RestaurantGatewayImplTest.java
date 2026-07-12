package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.RestaurantJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RestaurantGatewayImplTest {

    @Autowired
    private RestaurantJpaRepository repository;

    private RestaurantGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        gateway = new RestaurantGatewayImpl(repository);
    }

    private Restaurant newRestaurant(String name) {
        return Restaurant.create(name, "Rua das Flores, 123", KitchenType.ITALIAN,
                "08:00-22:00", UUID.randomUUID());
    }

    @Test
    void saveShouldPersistAndAssignId() {
        Restaurant saved = gateway.save(newRestaurant("Cantina da Nonna"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Cantina da Nonna");
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByIdShouldReturnSavedRestaurant() {
        Restaurant saved = gateway.save(newRestaurant("Sushi House"));

        assertThat(gateway.findById(saved.getId()))
                .isPresent()
                .get()
                .satisfies(restaurant -> {
                    assertThat(restaurant.getName()).isEqualTo("Sushi House");
                    assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.ITALIAN);
                    assertThat(restaurant.getOpeningHours()).isEqualTo("08:00-22:00");
                });
    }

    @Test
    void findByIdShouldReturnEmptyWhenAbsent() {
        assertThat(gateway.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findByNameShouldFilterByExactName() {
        gateway.save(newRestaurant("Cantina da Nonna"));
        gateway.save(newRestaurant("Sushi House"));

        List<Restaurant> found = gateway.findByName("Cantina da Nonna");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getName()).isEqualTo("Cantina da Nonna");
    }

    @Test
    void findByNameShouldMatchPartialName() {
        gateway.save(newRestaurant("Cantina da Nonna"));
        gateway.save(newRestaurant("Sushi House"));

        List<Restaurant> found = gateway.findByName("Cantina");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getName()).isEqualTo("Cantina da Nonna");
    }

    @Test
    void findByNameShouldBeCaseInsensitive() {
        gateway.save(newRestaurant("Cantina da Nonna"));

        List<Restaurant> found = gateway.findByName("cantina da nonna");

        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getName()).isEqualTo("Cantina da Nonna");
    }

    @Test
    void findAllShouldReturnEveryRestaurant() {
        gateway.save(newRestaurant("Cantina da Nonna"));
        gateway.save(newRestaurant("Sushi House"));

        assertThat(gateway.findAll()).hasSize(2);
    }

    @Test
    void updateShouldPersistChanges() {
        Restaurant saved = gateway.save(newRestaurant("Cantina da Nonna"));

        Restaurant changed = Restaurant.restore(saved.getId(), "Cantina Nova", "Nova Rua, 456",
                KitchenType.BRAZILIAN, "09:00-23:00", saved.getRestaurantOwnerId());
        Restaurant updated = gateway.update(changed);

        assertThat(updated.getName()).isEqualTo("Cantina Nova");
        assertThat(gateway.findById(saved.getId()))
                .get()
                .satisfies(restaurant -> {
                    assertThat(restaurant.getName()).isEqualTo("Cantina Nova");
                    assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.BRAZILIAN);
                });
    }

    @Test
    void deleteShouldRemoveRestaurant() {
        Restaurant saved = gateway.save(newRestaurant("Cantina da Nonna"));

        gateway.delete(saved.getId());

        assertThat(gateway.findById(saved.getId())).isEmpty();
    }

    @Test
    void existsByIdShouldReflectPresence() {
        Restaurant saved = gateway.save(newRestaurant("Cantina da Nonna"));

        assertThat(gateway.existsById(saved.getId())).isTrue();
        assertThat(gateway.existsById(UUID.randomUUID())).isFalse();
    }
}
