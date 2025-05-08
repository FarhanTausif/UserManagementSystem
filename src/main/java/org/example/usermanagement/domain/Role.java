package org.example.usermanagement.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Role {
    private final UUID id;
    private final String roleName;
    private final LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Role(String roleName) {
        this(UUID.randomUUID(), roleName);
    }

    public Role(UUID id, String roleName) {
        this(id, roleName, LocalDateTime.now(), LocalDateTime.now());
    }

    public Role(UUID id, String roleName, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.roleName = roleName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
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