package br.com.fiap.gastrohubapi.application.usecase.user;


import br.com.fiap.gastrohubapi.application.usecase.user.input.UpdateUserDTO;
import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;

public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public UpdateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public User execute(UpdateUserDTO dto) {
        User existingUser = this.userGateway.findById(dto.id())
                .orElseThrow(() -> new UserNotFoundException("User ID: " + dto.id() + " not found."));

        if (!existingUser.getEmail().equals(dto.email()) && this.userGateway.findByEmail(dto.email()).isPresent()) {
                throw new UserAlreadyExistsException("Email " + dto.email() + " is already in use.");
        }

        final UserType userType = this.userTypeGateway.findById(dto.userTypeId())
                .orElseThrow(() -> new UserTypeNotFoundException("User type ID: " + dto.userTypeId() + " not found."));

        existingUser.update(dto.name(), dto.email(), dto.password(), userType);

        return this.userGateway.update(existingUser);
    }
}
