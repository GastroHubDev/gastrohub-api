package br.com.fiap.gastrohubapi.application.gateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemGateway{
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(UUID id);
    List<MenuItem> findAll();
    List<MenuItem> findAllByRestaurantId(UUID restaurantId);
    MenuItem update(MenuItem menuItem);
    void delete(UUID id);
    boolean existsById(UUID id);
}
