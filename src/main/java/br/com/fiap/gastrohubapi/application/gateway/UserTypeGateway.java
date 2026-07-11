package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeGateway {
    UserType save(UserType userType);

    Optional<UserType> findById(UUID id);

    List<UserType> findAll();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean isInUse(UUID id);

    void deleteById(UUID id);
}
