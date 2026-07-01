package br.com.fiap.gastrohubapi.infrastructure.config;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway) {
        return new CreateUserUseCase(userGateway);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserGateway userGateway) {
        return new FindUserByIdUseCase(userGateway);
    }

    @Bean
    public FindUserByNameUseCase findUserByNameUseCase(UserGateway userGateway) {
        return new FindUserByNameUseCase(userGateway);
    }

    @Bean
    public FindUserByEmailUseCase findUserByEmailUseCase(UserGateway userGateway) {
        return new FindUserByEmailUseCase(userGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserGateway userGateway){
        return new UpdateUserUseCase(userGateway);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase(UserGateway userGateway) {
        return new FindAllUsersUseCase(userGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }
}