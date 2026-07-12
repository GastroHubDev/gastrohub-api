package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_type")
public class UserTypeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_category", nullable = false)
    private BaseCategory baseCategory;

    protected UserTypeJpaEntity() {
    }

    public UserTypeJpaEntity(UUID id) {
        this.id = id;
    }


    public UserTypeJpaEntity(UUID id, String name, BaseCategory baseCategory) {
        this.id = id;
        this.name = name;
        this.baseCategory = baseCategory;
    }

    public static UserTypeJpaEntity fromDomain(UserType userType) {
        return new UserTypeJpaEntity(
                userType.getId(),
                userType.getName(),
                userType.getBaseCategory()
        );
    }

    public UserType toDomain() {
        return UserType.restore(id, name, baseCategory);
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
}
