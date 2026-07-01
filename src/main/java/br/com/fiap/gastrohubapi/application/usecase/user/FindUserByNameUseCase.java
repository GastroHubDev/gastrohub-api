package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;

import java.util.List;

public class FindUserByNameUseCase {

    private final UserGateway userGateway;

    public FindUserByNameUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;

    }

    public static FindUserByNameUseCase create(UserGateway userGateway) {
        return new FindUserByNameUseCase(userGateway);
    }

    public List<User> run(String name){

        return this.userGateway.findByName(name);
    }
}
