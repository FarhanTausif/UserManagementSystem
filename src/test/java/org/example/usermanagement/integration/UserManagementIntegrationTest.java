package org.example.usermanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.usermanagement.infrastructure.controller.dto.CreateUserRequestDTO;
import org.example.usermanagement.infrastructure.controller.dto.CreateRoleRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequestDTO request = new CreateUserRequestDTO("John Doe", "john.doe@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")));
    }

    @Test
    @Transactional
    public void shouldCreateRoleSuccessfully() throws Exception {
        CreateRoleRequestDTO request = new CreateRoleRequestDTO("ADMIN");

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")));
    }

    @Test
    @Transactional
    public void shouldGetUserWithRolesSuccessfully() throws Exception {
        // Create user
        CreateUserRequestDTO userRequest = new CreateUserRequestDTO("Jane Smith", "jane.smith@example.com");
        String userResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UUID userId = UUID.fromString(userResponse.replace("\"", ""));

        // Create role
        CreateRoleRequestDTO roleRequest = new CreateRoleRequestDTO("USER");
        String roleResponse = mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UUID roleId = UUID.fromString(roleResponse.replace("\"", ""));

        // Assign role to user
        mockMvc.perform(post("/users/{userId}/assign-role/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(content().string("Role assigned successfully"));

        // Get user and verify role assignment
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.name", is("Jane Smith")))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].roleName", is("USER")))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.updatedDate", notNullValue()));
    }

    @Test
    @Transactional
    public void shouldGetUsersWithPagination() throws Exception {
        // Create multiple users
        for (int i = 1; i <= 5; i++) {
            CreateUserRequestDTO request = new CreateUserRequestDTO("User " + i, "user" + i + "@example.com");
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Test pagination
        mockMvc.perform(get("/users?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(5)))
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    @Transactional
    public void shouldRemoveRoleFromUserSuccessfully() throws Exception {
        // Create user
        CreateUserRequestDTO userRequest = new CreateUserRequestDTO("Test User", "test@example.com");
        String userResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UUID userId = UUID.fromString(userResponse.replace("\"", ""));

        // Create role
        CreateRoleRequestDTO roleRequest = new CreateRoleRequestDTO("TEMP_ROLE");
        String roleResponse = mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UUID roleId = UUID.fromString(roleResponse.replace("\"", ""));

        // Assign role to user
        mockMvc.perform(post("/users/{userId}/assign-role/{roleId}", userId, roleId))
                .andExpect(status().isOk());

        // Remove role from user
        mockMvc.perform(delete("/users/{userId}/remove-role/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(content().string("Role removed successfully"));

        // Verify role was removed
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles", hasSize(0)));
    }

    @Test
    @Transactional
    public void shouldReturn400ForInvalidUserData() throws Exception {
        CreateUserRequestDTO request = new CreateUserRequestDTO("", "invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void shouldReturn404ForNonExistentUser() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/users/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
