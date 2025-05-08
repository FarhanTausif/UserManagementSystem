package org.example.usermanagement.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequestDTO(
        @NotBlank(message = "Role name cannot be blank")
        String roleName
) {}
