package org.pozvezd.springuserapi.service;

import org.pozvezd.springuserapi.exception.DuplicatePhoneNumberException;
import org.pozvezd.springuserapi.exception.RoleNotFoundException;
import org.pozvezd.springuserapi.exception.UserNotFoundException;
import org.pozvezd.springuserapi.model.dto.request.CreateUserRequest;
import org.pozvezd.springuserapi.model.dto.request.UpdateUserRequest;
import org.pozvezd.springuserapi.model.dto.response.UserResponse;
import org.pozvezd.springuserapi.model.entity.UserEntity;
import org.pozvezd.springuserapi.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Transactional
    @CacheEvict(value = "users", key = "#result.uuid")
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (createUserRequest.getPhoneNumber() != null &&
        userRepository.existsByPhoneNumber(createUserRequest.getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("Phone number already exists: " + createUserRequest.getPhoneNumber());
        }

        UserEntity user = new UserEntity();
        user.setFio(createUserRequest.getFio());
        user.setPhoneNumber(createUserRequest.getPhoneNumber());
        user.setAvatar(createUserRequest.getAvatar());

        try {
            user.setRole(roleService.findRoleByName(createUserRequest.getRoleName()));
        } catch (RuntimeException e) {
            throw new RoleNotFoundException("Role not found: " + createUserRequest.getRoleName());
        }

        UserEntity savedUser = userRepository.save(user);
        return convertToResponse(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#uuid")
    public UserResponse getUserById(UUID uuid) {
        UserEntity user = userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + uuid));
        return convertToResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#updateUserRequest.userUuid")
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        UserEntity user = userRepository.findById(updateUserRequest.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User not found with UUID: " + updateUserRequest.getUserUuid()));

        if (updateUserRequest.getFio() != null) {
            user.setFio(updateUserRequest.getFio());
        }
        if (updateUserRequest.getPhoneNumber() != null) {

            if (userRepository.existsByPhoneNumber(updateUserRequest.getPhoneNumber()) &&
            !updateUserRequest.getPhoneNumber().equals(user.getPhoneNumber())) {
                throw new DuplicatePhoneNumberException("Phone number already exists");
            }
            user.setPhoneNumber(updateUserRequest.getPhoneNumber());
        }
        if (updateUserRequest.getAvatar() != null) {
            user.setAvatar(updateUserRequest.getAvatar());
        }
        if (updateUserRequest.getRoleName() != null) {
            user.setRole(roleService.findRoleByName(updateUserRequest.getRoleName()));
        }

        UserEntity updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#uuid")
    public void deleteUser(UUID uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new UserNotFoundException("User not found with UUID: " + uuid);
        }
        userRepository.deleteById(uuid);
    }

    private UserResponse convertToResponse(UserEntity user) {
        UserResponse response = new UserResponse();
        response.setUuid(user.getUuid());
        response.setFio(user.getFio());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAvatar(user.getAvatar());

        UserResponse.RoleResponse roleResponse = new UserResponse.RoleResponse();
        roleResponse.setUuid(user.getRole().getUuid());
        roleResponse.setRoleName(user.getRole().getRoleName());
        response.setRole(roleResponse);

        return response;
    }

}
