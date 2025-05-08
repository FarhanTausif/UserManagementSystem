package org.example.usermanagement.infrastructure.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        List<RoleResponseDTO> roles,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {}
