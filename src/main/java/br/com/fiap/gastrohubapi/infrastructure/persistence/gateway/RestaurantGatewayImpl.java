package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.RestaurantJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.RestaurantJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RestaurantGatewayImpl implements RestaurantGateway {

    private final RestaurantJpaRepository repository;

    private RestaurantGatewayImpl(RestaurantJpaRepository repository) {
        this.repository = repository;
    }

    public static RestaurantGatewayImpl create(RestaurantJpaRepository repository) {
        return new RestaurantGatewayImpl(repository);
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
        RestaurantJpaEntity entity = RestaurantJpaEntity.fromDomain(restaurant);
        return repository.save(entity).toDomain();
    }
}
