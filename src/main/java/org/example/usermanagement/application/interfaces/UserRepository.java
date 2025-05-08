package org.example.usermanagement.application.interfaces;

import org.example.usermanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public User save(User user);
    public Optional<User> findById(UUID id);
    public Page<User> findAll(Pageable pageable);
}