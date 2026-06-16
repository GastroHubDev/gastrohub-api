package br.com.fiap.gastrohubapi.infrastructure.persistence.gateway;

import br.com.fiap.gastrohubapi.application.gateway.UserTypeGateway;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public Optional<UserType> findById(Long id) {
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
    public boolean existsByNameAndIdNot(String name, Long id) {
        return repository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public boolean isInUse(Long id) {
        return false; //ToDo, will be changed
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}