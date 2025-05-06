package org.example.usermanagement.application;

import org.example.usermanagement.application.interfaces.UserRepository;
import org.example.usermanagement.application.interfaces.RoleRepository;
import org.example.usermanagement.domain.User;
import org.example.usermanagement.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User("Farhan Tausif", "bsse1440@iit.du.ac.bd");
        role = new Role("ADMIN");
    }

    @Test
    void createUser_validInput_returnsUserId() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UUID userId = userService.createUser("Farhan Tausif", "bsse1440@iit.du.ac.bd");
        assertNotNull(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_blankName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("", "bsse1440@iit.du.ac.bd"));
    }

    @Test
    void createUser_nullName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null, "bsse1440@iit.du.ac.bd"));
    }

    @Test
    void createUser_invalidEmail_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Farhan Tausif", "invalid-email"));
    }

    @Test
    void createUser_nullEmail_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("Farhan Tausif", null));
    }

    @Test
    void assignRoleToUser_invalidUserId_throwsException() {
        UUID invalidUserId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.assignRoleToUser(invalidUserId, roleId));
    }

    @Test
    void assignRoleToUser_invalidRoleId_throwsException() {
        UUID userId = UUID.randomUUID();
        UUID invalidRoleId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(invalidRoleId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.assignRoleToUser(userId, invalidRoleId));
    }

    @Test
    void getUserDetails_invalidUserId_throwsException() {
        UUID invalidUserId = UUID.randomUUID();
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserDetails(invalidUserId));
    }
}