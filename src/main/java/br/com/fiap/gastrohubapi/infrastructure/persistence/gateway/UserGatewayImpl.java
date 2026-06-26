package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;

    public UserGatewayImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return this.userRepository.findById(uuid)
                .map(this::mapToDomain);
    }

    @Override
    public User findByName(String name) {
        UserJpaEntity entity = this.userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("User " + name + " not found"));

        return mapToDomain(entity);
    }

    @Override
    public User add(User newUser) {
        UserJpaEntity entityToSave = new UserJpaEntity(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getPassword(), UserTypeJpaEntity.fromDomain(newUser.getUserType())
        );

        UserJpaEntity savedEntity = this.userRepository.save(entityToSave);

        return mapToDomain(savedEntity);
    }

    private User mapToDomain(UserJpaEntity entity) {
        return User.restore(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserType().toDomain()
        );
    }
}