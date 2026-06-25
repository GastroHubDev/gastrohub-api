package br.com.fiap.gastrohubapi.infrastructure.persistence.repository;

import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {
    List<RestaurantJpaEntity> findByName(String name);
}
