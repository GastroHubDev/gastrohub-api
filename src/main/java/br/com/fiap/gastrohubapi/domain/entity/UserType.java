package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;

public class UserType {
    private Long id;
    private String name;
    private BaseCategory baseCategory;


    public UserType(Long id, String name, BaseCategory baseCategory) {
        validateName(name);
        validateBaseCategory(baseCategory);

        this.id = id;
        this.name = name.trim();
        this.baseCategory = baseCategory;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BaseCategory getBaseCategory() {
        return baseCategory;
    }

    public void update(String name, BaseCategory baseCategory) {
        validateName(name);
        validateBaseCategory(baseCategory);

        this.name = name.trim();
        this.baseCategory = baseCategory;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserTypeException("User type name is required.");
        }
    }

    private void validateBaseCategory(BaseCategory baseCategory) {
        if (baseCategory == null) {
            throw new InvalidUserTypeException("Base category is required.");
        }
    }
}
