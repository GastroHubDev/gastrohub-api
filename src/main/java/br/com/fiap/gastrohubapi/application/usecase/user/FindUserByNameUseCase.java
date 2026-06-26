package br.com.fiap.gastrohubapi.application.usecase.user;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;

public class FindUserByNameUseCase {

    private final UserGateway userGateway;

    public FindUserByNameUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;

    }

    public static FindUserByNameUseCase create(UserGateway userGateway) {
        return new FindUserByNameUseCase(userGateway);
    }

    public User run(String name){
      User user = this.userGateway.findByName(name) ;
      if(user == null) {
          throw new UserNotFoundException("User name: " + name);
      }

      return user;

    }
}
