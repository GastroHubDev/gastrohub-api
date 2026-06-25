package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.IUserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;

import java.util.UUID;

public class FindUserByIdUseCase {

    private final IUserGateway userGateway;

    public FindUserByIdUseCase(IUserGateway userGateway) {
        this.userGateway = userGateway;

    }

    public static FindUserByIdUseCase create(IUserGateway userGateway) {
        return new FindUserByIdUseCase(userGateway);
    }

    public User run(UUID id){
        User user = this.userGateway.findById(id) ;
        if(user == null) {
            throw new UserNotFoundException("User ID: " + id);
        }

        return user;

    }
}
