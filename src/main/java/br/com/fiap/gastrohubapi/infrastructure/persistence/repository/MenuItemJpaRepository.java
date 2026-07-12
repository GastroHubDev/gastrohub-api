package br.com.fiap.gastrohubapi.infrastructure.persistence.repository;

import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID> {
    List<MenuItemJpaEntity> findAllByRestaurantId(UUID restaurantId);
}