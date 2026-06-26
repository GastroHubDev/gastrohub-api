
package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;

import java.util.Optional;

public class CreateUserUseCase {

    UserGateway userGateway;

    private CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }


    public static CreateUserUseCase create(UserGateway userGateway){
        return new CreateUserUseCase(userGateway);
    }

    public User run(NewUserDTO newUserDTO) {
        final Optional<User> existingUser = this.userGateway.findById(newUserDTO.id());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User " + newUserDTO.name() + " already exists.");
        }

        final User newUser = User.create(
                newUserDTO.name(),
                newUserDTO.email(),
                newUserDTO.password(),
                newUserDTO.userType()
        );

        return this.userGateway.add(newUser);
    }
}
