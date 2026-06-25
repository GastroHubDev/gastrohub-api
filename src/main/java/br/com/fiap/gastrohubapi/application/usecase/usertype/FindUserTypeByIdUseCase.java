package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;

public class FindUserTypeByIdUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(Long id) {
        return userTypeGateway.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));
    }
}