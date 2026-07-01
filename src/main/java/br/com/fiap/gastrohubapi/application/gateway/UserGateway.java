package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User save(User user);
    Optional<User> findById(UUID uuid);
    User update(User user);

    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void delete(UUID id);
}
