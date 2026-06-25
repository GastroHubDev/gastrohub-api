package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.domain.entity.User;

import java.util.UUID;

public interface IUserGateway {
    User findById(UUID uuid);

    User findByName(String name);

    User add(User newUser);

}
