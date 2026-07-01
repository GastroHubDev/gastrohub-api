package br.com.fiap.gastrohubapi.application.usecase.user;


import br.com.fiap.gastrohubapi.application.usecase.user.input.UpdateUserDTO;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;

public class UpdateUserUseCase {

    private final UserGateway userGateway;

    public UpdateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(UpdateUserDTO dto) {
        // 1. Verifica se o usuário existe
        User existingUser = this.userGateway.findById(dto.id())
                .orElseThrow(() -> new UserNotFoundException("User ID: " + dto.id() + " not found."));

        // 2. Se ele estiver tentando trocar o e-mail, garante que o novo e-mail já não existe
        if (!existingUser.getEmail().equals(dto.email())) {
            if (this.userGateway.findByEmail(dto.email()).isPresent()) {
                throw new UserAlreadyExistsException("Email " + dto.email() + " is already in use.");
            }
        }

        existingUser.update(dto.name(), dto.email(), dto.password(), dto.userType());

        return this.userGateway.update(existingUser);
    }
}