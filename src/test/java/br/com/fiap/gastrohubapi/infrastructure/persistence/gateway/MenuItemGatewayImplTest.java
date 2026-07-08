package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuItemGatewayImplTest {

    @Autowired
    private MenuItemRepository repository;

    private MenuItemGatewayImpl gateway;

    private final UUID restaurantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        gateway = new MenuItemGatewayImpl(repository);
    }

    private MenuItem newItem(String name, UUID restaurant) {
        return MenuItem.create(name, "desc " + name, new BigDecimal("25.00"),
                true, "/photos/" + name + ".jpg", restaurant);
    }

    @Test
    void saveShouldPersistAndAssignId() {
        MenuItem saved = gateway.save(newItem("Pizza", restaurantId));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Pizza");
        assertThat(saved.getPrice()).isEqualByComparingTo("25.00");
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByIdShouldReturnSavedItem() {
        MenuItem saved = gateway.save(newItem("Burger", restaurantId));

        assertThat(gateway.findById(saved.getId()))
                .isPresent()
                .get()
                .satisfies(item -> {
                    assertThat(item.getName()).isEqualTo("Burger");
                    assertThat(item.getDescription()).isEqualTo("desc Burger");
                    assertThat(item.isOnlyInRestaurant()).isTrue();
                    assertThat(item.getRestaurantId()).isEqualTo(restaurantId);
                });
    }

    @Test
    void findByIdShouldReturnEmptyWhenAbsent() {
        assertThat(gateway.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void findAllShouldReturnEveryItem() {
        gateway.save(newItem("Pizza", restaurantId));
        gateway.save(newItem("Burger", restaurantId));

        assertThat(gateway.findAll()).hasSize(2);
    }

    @Test
    void findAllByRestaurantIdShouldFilterByRestaurant() {
        UUID otherRestaurant = UUID.randomUUID();
        gateway.save(newItem("Pizza", restaurantId));
        gateway.save(newItem("Sushi", otherRestaurant));

        List<MenuItem> items = gateway.findAllByRestaurantId(restaurantId);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Pizza");
    }

    @Test
    void updateShouldPersistChanges() {
        MenuItem saved = gateway.save(newItem("Pizza", restaurantId));

        MenuItem changed = MenuItem.restore(saved.getId(), "Pizza Grande",
                "extra large", new BigDecimal("49.90"), false, null, restaurantId);
        MenuItem updated = gateway.update(changed);

        assertThat(updated.getName()).isEqualTo("Pizza Grande");
        assertThat(gateway.findById(saved.getId()))
                .get()
                .satisfies(item -> {
                    assertThat(item.getName()).isEqualTo("Pizza Grande");
                    assertThat(item.getPrice()).isEqualByComparingTo("49.90");
                    assertThat(item.isOnlyInRestaurant()).isFalse();
                });
    }

    @Test
    void deleteShouldRemoveItem() {
        MenuItem saved = gateway.save(newItem("Pizza", restaurantId));

        gateway.delete(saved.getId());

        assertThat(gateway.findById(saved.getId())).isEmpty();
    }

    @Test
    void existsByIdShouldReflectPresence() {
        MenuItem saved = gateway.save(newItem("Pizza", restaurantId));

        assertThat(gateway.existsById(saved.getId())).isTrue();
        assertThat(gateway.existsById(UUID.randomUUID())).isFalse();
    }
}
