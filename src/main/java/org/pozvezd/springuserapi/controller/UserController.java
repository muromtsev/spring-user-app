package org.pozvezd.springuserapi.controller;

import jakarta.validation.Valid;
import org.pozvezd.springuserapi.model.dto.request.CreateUserRequest;
import org.pozvezd.springuserapi.model.dto.request.UpdateUserRequest;
import org.pozvezd.springuserapi.model.dto.response.UserResponse;
import org.pozvezd.springuserapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("Received request to create new user: " + createUserRequest.getFio());
        UserResponse response = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser(@RequestParam UUID userUuid) {
        log.info("Received request to get user with UUID: " + userUuid);
        UserResponse response = userService.getUserById(userUuid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/userDetailsUpdate")
    public ResponseEntity<UserResponse> updateUserDetails(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        log.info("Received request to update user with UUID: " + updateUserRequest.getUserUuid());
        UserResponse response = userService.updateUser(updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@RequestParam UUID userUuid) {
        log.info("Received request to delete user with UUID: " + userUuid);
        userService.deleteUser(userUuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
