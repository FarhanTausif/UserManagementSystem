package org.example.usermanagement.infrastructure.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponseDTO(
        UUID id,
        String roleName,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
