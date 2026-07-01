
package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;

import java.util.Optional;

public class CreateUserUseCase {

    private final UserGateway userGateway;

    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }


    public static CreateUserUseCase create(UserGateway userGateway){
        return new CreateUserUseCase(userGateway);
    }

    public User run(NewUserDTO newUserDTO) {
        if(this.userGateway.findByEmail(newUserDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + newUserDTO.email() + " already exists.");
        }

        final User newUser = User.create(
                newUserDTO.name(),
                newUserDTO.email(),
                newUserDTO.password(),
                newUserDTO.userType()
        );

        return this.userGateway.save(newUser);
    }
}
