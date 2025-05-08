package org.example.usermanagement.infrastructure.persistence;

import org.example.usermanagement.application.interfaces.UserRepository;
import org.example.usermanagement.domain.User;
import org.example.usermanagement.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserJpaRepository implements UserRepository {
    private final UserSpringDataRepository springDataRepository;

    public UserJpaRepository(UserSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setCreatedDate(user.getCreatedDate());
        entity.setUpdatedDate(user.getUpdatedDate());
        entity.setRoles(user.getRoles().stream()
                .map(role -> {
                    RoleJpaEntity roleEntity = new RoleJpaEntity();
                    roleEntity.setId(role.getId());
                    roleEntity.setRoleName(role.getRoleName());
                    roleEntity.setCreatedDate(role.getCreatedDate());
                    roleEntity.setUpdatedDate(role.getUpdatedDate());
                    return roleEntity;
                })
                .collect(Collectors.toList()));
        UserJpaEntity savedEntity = springDataRepository.save(entity);
        User savedUser = new User(savedEntity.getId(), savedEntity.getName(), savedEntity.getEmail(),
                                 savedEntity.getCreatedDate(), savedEntity.getUpdatedDate());
        savedEntity.getRoles().forEach(roleEntity ->
                savedUser.assignRole(new Role(roleEntity.getId(), roleEntity.getRoleName(),
                                             roleEntity.getCreatedDate(), roleEntity.getUpdatedDate())));
        return savedUser;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataRepository.findById(id).map(entity -> {
            User user = new User(entity.getId(), entity.getName(), entity.getEmail(),
                               entity.getCreatedDate(), entity.getUpdatedDate());
            entity.getRoles().forEach(roleEntity ->
                    user.assignRole(new Role(roleEntity.getId(), roleEntity.getRoleName(),
                                           roleEntity.getCreatedDate(), roleEntity.getUpdatedDate())));
            return user;
        });
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return springDataRepository.findAll(pageable).map(entity -> {
            User user = new User(entity.getId(), entity.getName(), entity.getEmail(),
                               entity.getCreatedDate(), entity.getUpdatedDate());
            entity.getRoles().forEach(roleEntity ->
                    user.assignRole(new Role(roleEntity.getId(), roleEntity.getRoleName(),
                                           roleEntity.getCreatedDate(), roleEntity.getUpdatedDate())));
            return user;
        });
    }
}