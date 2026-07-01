package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "user_type")
public class UserTypeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_category", nullable = false)
    private BaseCategory baseCategory;

    protected UserTypeJpaEntity() {
    }

    public UserTypeJpaEntity(Long id) {
        this.id = id;
    }


    public UserTypeJpaEntity(Long id, String name, BaseCategory baseCategory) {
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
        return new UserType(id, name, baseCategory);
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
}