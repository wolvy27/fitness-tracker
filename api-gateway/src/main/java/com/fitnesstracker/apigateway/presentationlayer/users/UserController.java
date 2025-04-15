package com.fitnesstracker.apigateway.presentationlayer.users;

import com.fitnesstracker.apigateway.businesslayer.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(
            value = "/{userId}",
            produces = "application/json"
    )
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
        log.info("getUser: userId={}", userId);
        return ResponseEntity.ok().body(userService.getUserByUserId(userId));
    }

    @GetMapping(value = "application/json")
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
}
