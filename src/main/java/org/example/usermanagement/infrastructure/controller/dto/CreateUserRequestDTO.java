package org.example.usermanagement.infrastructure.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email
) {}
