package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ToDo validate here
    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypeJpaEntity userType;

    public UserJpaEntity() {
    }

    public UserJpaEntity(UUID id, String name, String email, String password, UserTypeJpaEntity userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserTypeJpaEntity getUserType() {
        return userType;
    }
}