package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;

public class CreateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public CreateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(String name, BaseCategory baseCategory) {
        String normalizedName = name == null ? null : name.trim();

        if (normalizedName != null && userTypeGateway.existsByName(normalizedName)) {
            throw new DuplicateUserTypeNameException("User type name already exists.");
        }

        UserType userType = new UserType(null, normalizedName, baseCategory);

        return userTypeGateway.save(userType);
    }
}
