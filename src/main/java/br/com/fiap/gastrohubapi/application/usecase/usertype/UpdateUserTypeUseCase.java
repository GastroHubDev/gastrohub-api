package br.com.fiap.gastrohubapi.application.usecase.usertype;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.domain.exception.DuplicateUserTypeNameException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeInUseException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotFoundException;

import java.util.UUID;

public class UpdateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public UpdateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(UUID id, String name, BaseCategory baseCategory) {
        UserType userType = userTypeGateway.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException("User type not found."));

        String normalizedName = name == null ? null : name.trim();

        if (normalizedName != null && userTypeGateway.existsByNameAndIdNot(normalizedName, id)) {
            throw new DuplicateUserTypeNameException("User type name already exists.");
        }

        if (!userType.getBaseCategory().equals(baseCategory) && userTypeGateway.isInUse(id)) {
            throw new UserTypeInUseException("Cannot change base category because user type is in use.");
        }

        userType.update(normalizedName, baseCategory);

        return userTypeGateway.save(userType);
    }
}