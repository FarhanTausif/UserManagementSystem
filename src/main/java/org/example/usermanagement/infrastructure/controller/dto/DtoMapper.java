package org.example.usermanagement.infrastructure.controller.dto;

import org.example.usermanagement.domain.Role;
import org.example.usermanagement.domain.User;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class DtoMapper {
    
    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(DtoMapper::toRoleResponseDTO)
                        .collect(Collectors.toList()),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }
    
    public static RoleResponseDTO toRoleResponseDTO(Role role) {
        return new RoleResponseDTO(
                role.getId(),
                role.getRoleName(),
                role.getCreatedDate(),
                role.getUpdatedDate()
        );
    }
    
    public static Page<UserResponseDTO> toUserResponseDTOPage(Page<User> userPage) {
        return userPage.map(DtoMapper::toUserResponseDTO);
    }
}
