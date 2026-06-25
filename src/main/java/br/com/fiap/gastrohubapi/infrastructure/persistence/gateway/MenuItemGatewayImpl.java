package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.MenuItemJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.MenuItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MenuItemGatewayImpl implements MenuItemGateway {

    private final MenuItemRepository repository;

    public MenuItemGatewayImpl(MenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity saved = repository.save(toJpaEntity(menuItem));
        return toDomainEntity(saved);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<MenuItem> findAll() {
        return repository.findAll().stream().map(this::toDomainEntity).toList();
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(UUID restaurantId) {
        return repository.findAllByRestaurantId(restaurantId).stream()
                .map(this::toDomainEntity).toList();
    }

    @Override
    public MenuItem update(MenuItem menuItem) {
        MenuItemJpaEntity updated = repository.save(toJpaEntity(menuItem));
        return toDomainEntity(updated);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private MenuItemJpaEntity toJpaEntity(MenuItem menuItem) {
        return new MenuItemJpaEntity(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurantId()
        );
    }

    private MenuItem toDomainEntity(MenuItemJpaEntity jpaEntity) {
        return new MenuItem(
                jpaEntity.getId(),
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                jpaEntity.getPrice(),
                jpaEntity.isOnlyInRestaurant(),
                jpaEntity.getPhotoPath(),
                jpaEntity.getRestaurantId()
        );
    }
}
