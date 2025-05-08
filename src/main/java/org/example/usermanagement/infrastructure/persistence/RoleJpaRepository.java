package org.example.usermanagement.infrastructure.persistence;

import org.example.usermanagement.application.interfaces.RoleRepository;
import org.example.usermanagement.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleJpaRepository implements RoleRepository {
    private final RoleSpringDataRepository springDataRepository;

    public RoleJpaRepository(RoleSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = new RoleJpaEntity();
        entity.setId(role.getId());
        entity.setRoleName(role.getRoleName());
        entity.setCreatedDate(role.getCreatedDate());
        entity.setUpdatedDate(role.getUpdatedDate());
        RoleJpaEntity savedEntity = springDataRepository.save(entity);
        return new Role(savedEntity.getId(), savedEntity.getRoleName(),
                       savedEntity.getCreatedDate(), savedEntity.getUpdatedDate());
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return springDataRepository.findById(id).map(entity ->
                new Role(entity.getId(), entity.getRoleName(),
                        entity.getCreatedDate(), entity.getUpdatedDate())
        );
    }
}