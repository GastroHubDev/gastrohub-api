
package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.application.gateway.IUserGateway;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.presentation.dto.request.NewUserDTO;

public class CreateUserUseCase {

    IUserGateway userGateway;

    private CreateUserUseCase(IUserGateway userGateway) {
        this.userGateway = userGateway;
    }


    public static CreateUserUseCase create(IUserGateway userGateway){
        return new CreateUserUseCase(userGateway);
    }

    public User run(NewUserDTO newUserDTO){
        final User ExistingUser = this.userGateway.findById(newUserDTO.id());

        if(ExistingUser != null) {
            throw new UserAlreadyExistsException("User " + newUserDTO.name() + "is already exists.");
        }

        final User newUser = User.create(newUserDTO.name(), newUserDTO.email(), newUserDTO.password(), newUserDTO.userType());

        User user = this.userGateway.add(newUser);
        return user;
    }
}
