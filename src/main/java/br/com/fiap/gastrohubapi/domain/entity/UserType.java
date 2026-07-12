package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;

import java.util.UUID;

public class UserType {
    private UUID id;
    private String name;
    private BaseCategory baseCategory;

    private UserType() {}

    public static UserType create(String name, BaseCategory baseCategory) {
        UserType userType = new UserType();
        userType.setName(name);
        userType.setBaseCategory(baseCategory);

        return userType;
    }

    public static UserType restore(UUID id, String name, BaseCategory baseCategory) {
        UserType userType = new UserType();
        userType.setId(id);
        userType.setName(name);
        userType.setBaseCategory(baseCategory);

        return userType;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BaseCategory getBaseCategory() {
        return baseCategory;
    }

    public void update(String name, BaseCategory baseCategory) {
        setName(name);
        setBaseCategory(baseCategory);
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserTypeException("User type name is required.");
        }
    }

    private static void validateBaseCategory(BaseCategory baseCategory) {
        if (baseCategory == null) {
            throw new InvalidUserTypeException("Base category is required.");
        }
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    private void setBaseCategory(BaseCategory baseCategory) {
        validateBaseCategory(baseCategory);
        this.baseCategory = baseCategory;
    }
}
