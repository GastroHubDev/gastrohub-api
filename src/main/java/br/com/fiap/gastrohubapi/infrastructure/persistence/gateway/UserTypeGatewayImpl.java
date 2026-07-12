package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserTypeGatewayImpl implements UserTypeGateway {

    private final UserTypeJpaRepository repository;

    public UserTypeGatewayImpl(UserTypeJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserType save(UserType userType) {
        UserTypeJpaEntity entity = UserTypeJpaEntity.fromDomain(userType);
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return repository.findById(id)
                .map(UserTypeJpaEntity::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll()
                .stream()
                .map(UserTypeJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, UUID id) {
        return repository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean isInUse(UUID id) {
        return false; //ToDo, will be changed
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
