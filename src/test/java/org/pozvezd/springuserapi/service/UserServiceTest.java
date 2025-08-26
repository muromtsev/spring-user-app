package org.pozvezd.springuserapi.service;


import org.pozvezd.springuserapi.model.dto.request.CreateUserRequest;
import org.pozvezd.springuserapi.model.dto.request.UpdateUserRequest;
import org.pozvezd.springuserapi.model.entity.RoleEntity;
import org.pozvezd.springuserapi.model.entity.UserEntity;
import org.pozvezd.springuserapi.repository.UserRepository;
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
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest createUserRequest;
    private RoleEntity roleEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setFio("Иванов Иван Иванович");
        createUserRequest.setPhoneNumber("+79161234567");
        createUserRequest.setAvatar("https://example.com/avatar.jpg");
        createUserRequest.setRoleName("USER");

        roleEntity = new RoleEntity();
        roleEntity.setUuid(UUID.randomUUID());
        roleEntity.setRoleName("USER");

        userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID());
        userEntity.setFio("Иванов Иван Иванович");
        userEntity.setPhoneNumber("+79161234567");
        userEntity.setAvatar("https://example.com/avatar.jpg");
        userEntity.setRole(roleEntity);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(roleService.findRoleByName("USER")).thenReturn(roleEntity);
        when(userRepository.existsByPhoneNumber("+79161234567")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        var response = userService.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals("Иванов Иван Иванович", response.getFio());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberExists() {
        when(userRepository.existsByPhoneNumber("+79161234567")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        var response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals("Иванов Иван Иванович", response.getFio());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setUserUuid(userId);
        updateRequest.setFio("Обновленное Имя");
        updateRequest.setRoleName("ADMIN");

        RoleEntity adminRole = new RoleEntity();
        adminRole.setUuid(UUID.randomUUID());
        adminRole.setRoleName("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(roleService.findRoleByName("ADMIN")).thenReturn(adminRole);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        var response = userService.updateUser(updateRequest);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> {
            userService.deleteUser(userId);
        });

        verify(userRepository, times(1)).deleteById(userId);
    }
}
