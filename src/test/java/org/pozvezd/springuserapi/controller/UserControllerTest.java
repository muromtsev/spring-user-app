package org.pozvezd.springuserapi.controller;

import org.pozvezd.springuserapi.model.dto.request.CreateUserRequest;
import org.pozvezd.springuserapi.model.dto.request.UpdateUserRequest;
import org.pozvezd.springuserapi.model.dto.response.UserResponse;
import org.pozvezd.springuserapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setFio("Иванов Иван Иванович");
        request.setPhoneNumber("+79161234567");
        request.setRoleName("USER");

        UserResponse response = new UserResponse();
        response.setUuid(UUID.randomUUID());
        response.setFio("Иванов Иван Иванович");

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/createNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fio").value("Иванов Иван Иванович"));
    }

    @Test
    void shouldGetUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse response = new UserResponse();
        response.setUuid(userId);
        response.setFio("Иванов Иван Иванович");

        when(userService.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/api/users")
                        .param("userUuid", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userId.toString()))
                .andExpect(jsonPath("$.fio").value("Иванов Иван Иванович"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUserUuid(userId);
        request.setFio("Обновленное Имя");

        UserResponse response = new UserResponse();
        response.setUuid(userId);
        response.setFio("Обновленное Имя");

        when(userService.updateUser(any(UpdateUserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/userDetailsUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Обновленное Имя"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/users")
                        .param("userUuid", userId.toString()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }
}
