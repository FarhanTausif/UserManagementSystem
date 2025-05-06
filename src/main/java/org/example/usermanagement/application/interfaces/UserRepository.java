package org.example.usermanagement.application.interfaces;

import org.example.usermanagement.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public User save(User user);
    public Optional<User> findById(UUID id);
}