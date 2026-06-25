package br.com.fiap.gastrohubapi.infrastructure.config;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.application.usecase.usertype.CreateUserTypeUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.DeleteUserTypeUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.FindUserTypeByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.ListUserTypesUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.UpdateUserTypeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeUseCaseConfig {

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(UserTypeGateway userTypeGateway) {
        return new CreateUserTypeUseCase(userTypeGateway);
    }

    @Bean
    public FindUserTypeByIdUseCase findUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
        return new FindUserTypeByIdUseCase(userTypeGateway);
    }

    @Bean
    public ListUserTypesUseCase listUserTypesUseCase(UserTypeGateway userTypeGateway) {
        return new ListUserTypesUseCase(userTypeGateway);
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        return new UpdateUserTypeUseCase(userTypeGateway);
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase(UserTypeGateway userTypeGateway) {
        return new DeleteUserTypeUseCase(userTypeGateway);
    }
}