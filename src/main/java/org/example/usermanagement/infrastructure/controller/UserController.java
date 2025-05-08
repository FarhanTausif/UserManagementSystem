package org.example.usermanagement.infrastructure.controller;

import org.example.usermanagement.application.UserService;
import org.example.usermanagement.infrastructure.controller.dto.CreateUserRequestDTO;
import org.example.usermanagement.infrastructure.controller.dto.DtoMapper;
import org.example.usermanagement.infrastructure.controller.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UUID> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        UUID userId = userService.createUser(request.name(), request.email());
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        Page<UserResponseDTO> users = DtoMapper.toUserResponseDTOPage(
                userService.getAllUsers(pageable));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        UserResponseDTO user = DtoMapper.toUserResponseDTO(
                userService.getUserDetails(id));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/assign-role/{roleId}")
    public ResponseEntity<String> assignRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping("/{userId}/remove-role/{roleId}")
    public ResponseEntity<String> removeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok("Role removed successfully");
    }
}


