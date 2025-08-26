package org.pozvezd.springuserapi;

import org.junit.jupiter.api.Order;
import org.pozvezd.springuserapi.model.dto.request.CreateUserRequest;
import org.pozvezd.springuserapi.model.dto.request.UpdateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pozvezd.springuserapi.model.entity.RoleEntity;
import org.pozvezd.springuserapi.repository.RoleRepository;
import org.pozvezd.springuserapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest createUserRequest;

    private String createTestUser(String fio, String phone, String role) throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setFio(fio);
        request.setPhoneNumber(phone);
        request.setAvatar("https://example.com/avatar.jpg");
        request.setRoleName(role);

        MvcResult result = mockMvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("uuid").asText();
    }

    @BeforeEach
    void cleanup() {

        userRepository.deleteAll();

        if (roleRepository.count() == 0) {
            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);

            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName("ADMIN");
            roleRepository.save(adminRole);

            RoleEntity moderatorRole = new RoleEntity();
            moderatorRole.setRoleName("MODERATOR");
            roleRepository.save(moderatorRole);
        }
    }

    @Test
    void shouldCreateUser() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setFio("Иван Иванов");
        request.setPhoneNumber("+79998887766");
        request.setAvatar("https://example.com/avatar.jpg");
        request.setRoleName("USER");

        mockMvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fio").value("Иван Иванов"))
                .andExpect(jsonPath("$.phoneNumber").value("+79998887766"))
                .andExpect(jsonPath("$.role.roleName").value("USER"));
    }

    @Test
    void shouldGetUserById() throws Exception {

        String userId = createTestUser("Иванов Иван", "+79990000001", "USER");

        mockMvc.perform(get("/api/users")
                        .param("userUuid", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userId))
                .andExpect(jsonPath("$.fio").value("Иванов Иван"))
                .andExpect(jsonPath("$.phoneNumber").value("+79990000001"));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        String userId = createTestUser("Иванов Иван", "+79160000003", "USER");

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUserUuid(UUID.fromString(userId));
        updateRequest.setFio("Петров Петр");
        updateRequest.setPhoneNumber("+79160000004");
        updateRequest.setRoleName("ADMIN");

        mockMvc.perform(put("/api/userDetailsUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Петров Петр"))
                .andExpect(jsonPath("$.phoneNumber").value("+79160000004"))
                .andExpect(jsonPath("$.role.roleName").value("ADMIN"));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        String userId = createTestUser("Сидоров Алексей", "+79160000005", "USER");

      mockMvc.perform(delete("/api/users")
                    .param("userUuid", userId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users")
                        .param("userUuid", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        String nonExistentId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/users")
                        .param("userUuid", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidData() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setFio("");
        invalidRequest.setRoleName("USER");

        mockMvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldReturnConflictForDuplicatePhone() throws Exception {

        createTestUser("Иванов Иван", "+79160000006", "USER");

        CreateUserRequest duplicateRequest = new CreateUserRequest();
        duplicateRequest.setFio("Петров Петр");
        duplicateRequest.setPhoneNumber("+79160000006");
        duplicateRequest.setRoleName("USER");

        mockMvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }
}
