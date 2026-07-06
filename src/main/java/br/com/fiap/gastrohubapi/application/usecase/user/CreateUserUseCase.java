
package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.UserAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public CreateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }


    public static CreateUserUseCase create(UserGateway userGateway, UserTypeGateway userTypeGateway){
        return new CreateUserUseCase(userGateway, userTypeGateway);
    }

    public User run(NewUserDTO newUserDTO) {
        if(this.userGateway.findByEmail(newUserDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + newUserDTO.email() + " already exists.");
        }

        final UserType userType = this.userTypeGateway.findById(newUserDTO.userTypeId())
                .orElseThrow(() -> new UserTypeNotFoundException("User type ID: " + newUserDTO.userTypeId() + " not found."));

        final User newUser = User.create(
                newUserDTO.name(),
                newUserDTO.email(),
                newUserDTO.password(),
                userType
        );

        return this.userGateway.save(newUser);
    }
}
