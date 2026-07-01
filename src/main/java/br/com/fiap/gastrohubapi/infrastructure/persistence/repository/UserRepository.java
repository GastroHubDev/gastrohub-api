package br.com.fiap.gastrohubapi.infrastructure.persistence.repository;

import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, UUID> {
    List<UserJpaEntity> findByName(String name);
    Optional<UserJpaEntity> findByEmail(String name);

}