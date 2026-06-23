package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.RestaurantJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.RestaurantJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantGatewayImpl implements RestaurantGateway {

    private final RestaurantJpaRepository repository;

    public RestaurantGatewayImpl(RestaurantJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Restaurant> findById(UUID uuid) {
        return repository.findById(uuid)
                .map(RestaurantJpaEntity::toDomain);
    }

    @Override
    public List<Restaurant> findByName(String name) {
        return repository.findByName(name)
                .stream().map(RestaurantJpaEntity::toDomain).toList();
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity entity = RestaurantJpaEntity.fromDomain(restaurant);
        return repository.save(entity).toDomain();
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        final RestaurantJpaEntity entity = RestaurantJpaEntity.fromDomain(restaurant);
        return repository.save(entity).toDomain();
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll()
                .stream().map(RestaurantJpaEntity::toDomain).toList();
    }
}
