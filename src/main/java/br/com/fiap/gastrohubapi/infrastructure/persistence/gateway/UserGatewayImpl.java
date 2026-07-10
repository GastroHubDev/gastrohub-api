package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserGatewayImpl implements UserGateway {

    private final UserJpaRepository userRepository;

    public UserGatewayImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return this.userRepository.findById(uuid)
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public List<User> findByName(String name) {
        return this.userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public User save(User newUser) {
        UserJpaEntity entityToSave = UserJpaEntity.fromDomain(newUser);

        return this.userRepository.save(entityToSave).toDomain();
    }

    @Override
    public User update(User updatedUser) {
        UserJpaEntity entityToUpdate = UserJpaEntity.fromDomain(updatedUser);
        entityToUpdate.markNotNew();

        return this.userRepository.save(entityToUpdate).toDomain();
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll()
                .stream()
                .map(UserJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        this.userRepository.deleteById(id);
    }

}
