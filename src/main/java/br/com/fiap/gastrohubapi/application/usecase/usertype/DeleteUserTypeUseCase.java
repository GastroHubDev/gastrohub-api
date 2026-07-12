package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;

import java.util.UUID;

public class DeleteUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public DeleteUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public void execute(UUID id) {
        userTypeGateway.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        if (userTypeGateway.isInUse(id)) {
            throw new UserTypeInUseException("Cannot delete user type because it is in use.");
        }

        userTypeGateway.deleteById(id);
    }
}