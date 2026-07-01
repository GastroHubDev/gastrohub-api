package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<User> findByName(String name) {
        return this.userRepository.findByName(name)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

//    @Override
//    public User findByName(String name) {
//        UserJpaEntity entity = this.userRepository.findByName(name)
//                .orElseThrow(() -> new UserNotFoundException("User " + name + " not found"));
//
//        return mapToDomain(entity);
//    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .map(this::mapToDomain);
    }

    @Override
    public User save(User newUser) {
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

    @Override
    public User update(User updatedUser) {

        // Passa o ID direto no construtor novo! Fim do problema.
        UserTypeJpaEntity typeJpa = new UserTypeJpaEntity(updatedUser.getUserType().getId());

        UserJpaEntity entityToUpdate = new UserJpaEntity(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPassword(),
                typeJpa
        );

        entityToUpdate.markNotNew();

        return mapToDomain(this.userRepository.save(entityToUpdate));
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        this.userRepository.deleteById(id);
    }

}