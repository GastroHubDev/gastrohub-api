package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import java.util.UUID;

public class DeleteUserUseCase {

    private final UserGateway userGateway;

    public DeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(UUID id) {
        User user = this.userGateway.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User ID: " + id + " not found."));

        this.userGateway.delete(user.getId());
    }
}