package com.fitnesstracker.apigateway.presentationlayer.users;

import com.fitnesstracker.apigateway.businesslayer.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
        log.info("getUser: userId={}", userId);
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        log.info("getUsers called");
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel) {
        log.debug("addUser called");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userRequestModel));
    }

    @PutMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable String userId,
                                                        @RequestBody UserRequestModel userRequestModel) {
        log.debug("updateUser called");
        return ResponseEntity.ok(userService.updateUser(userRequestModel, userId));
    }

    @DeleteMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.debug("deleteUser called");
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}