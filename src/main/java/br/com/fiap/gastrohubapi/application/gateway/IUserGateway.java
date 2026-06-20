package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserGateway {
    Optional<User> findById(UUID uuid);
}
