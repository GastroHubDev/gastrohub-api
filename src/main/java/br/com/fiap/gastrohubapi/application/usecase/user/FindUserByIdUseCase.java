package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;

import java.util.UUID;

public class FindUserByIdUseCase {

    private final UserGateway userGateway;

    public FindUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;

    }

    public static FindUserByIdUseCase create(UserGateway userGateway) {
        return new FindUserByIdUseCase(userGateway);
    }

    public User run(UUID id) {
        return this.userGateway.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User ID: " + id));
    }
}
