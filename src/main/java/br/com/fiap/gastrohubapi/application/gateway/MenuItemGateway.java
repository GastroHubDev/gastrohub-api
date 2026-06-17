package br.com.fiap.gastrohubapi.application.gateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import java.util.List;
import java.util.Optional;

public interface MenuItemGateway{
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(Long id);
    List<MenuItem> findAll();
    List<MenuItem> findAllByRestaurantId(Long restaurantId);
    MenuItem update(MenuItem menuItem);
    void delete(Long id);
    boolean existsById(Long id);
}
