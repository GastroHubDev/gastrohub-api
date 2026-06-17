package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {
    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    List<UserType> findAll();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean isInUse(Long id);

    void deleteById(Long id);
}
