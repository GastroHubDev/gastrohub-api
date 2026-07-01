package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;

public class FindUserByEmailUseCase {

    private final UserGateway userGateway;

    public FindUserByEmailUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String email) {
        return this.userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }
}