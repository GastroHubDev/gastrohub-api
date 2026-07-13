package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.List;

public class ListUserTypesUseCase {

    private final UserTypeGateway userTypeGateway;

    public ListUserTypesUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public List<UserType> execute() {
        return userTypeGateway.findAll();
    }
}