package org.example.usermanagement.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final List<Role> roles;
    private final LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public User(String name, String email) {
        this(UUID.randomUUID(), name, email);
    }

    public User(UUID id, String name, String email) {
        this(id, name, email, LocalDateTime.now(), LocalDateTime.now());
    }

    public User(UUID id, String name, String email, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = new ArrayList<>();
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void assignRole(Role role) {
        roles.add(role);
        this.updatedDate = LocalDateTime.now();
    }

    public void removeRole(Role role) {
        roles.removeIf(r -> r.getId().equals(role.getId()));
        this.updatedDate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoles() {
        return new ArrayList<>(roles);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void updateTimestamp() {
        this.updatedDate = LocalDateTime.now();
    }
}