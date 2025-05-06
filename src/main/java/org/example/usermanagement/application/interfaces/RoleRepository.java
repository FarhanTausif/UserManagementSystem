package org.example.usermanagement.application.interfaces;

import org.example.usermanagement.domain.Role;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    public Role save(Role role);
    public Optional<Role> findById(UUID id);
}