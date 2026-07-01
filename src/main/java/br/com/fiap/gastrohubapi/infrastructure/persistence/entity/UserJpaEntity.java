package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class UserJpaEntity implements Persistable<UUID> {

    @Id
    // A anotação @GeneratedValue foi removida para que o Domínio seja o dono do UUID
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Ajuste: FetchType.LAZY evita consultas pesadas desnecessárias (N+1 Query Problem)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypeJpaEntity userType;

    // Flag transitória para avisar o JPA quando é um INSERT ou um UPDATE
    @Transient
    private boolean isNewRecord = true;

    public UserJpaEntity() {
    }

    public UserJpaEntity(UUID id, String name, String email, String password, UserTypeJpaEntity userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewRecord;
    }

    @PostLoad
    @PostPersist
    public void markNotNew() {
        this.isNewRecord = false;
    }
    // --------------------------------------------------------------------------

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