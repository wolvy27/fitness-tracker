package com.fitnesstracker.users.presentationlayer;


import com.fitnesstracker.users.businesslayer.UserService;
import com.fitnesstracker.users.utils.exceptions.InvalidInputException;
import com.fitnesstracker.users.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private static final int UUID_LENGTH = 36;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseModel>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        UserResponseModel user = userService.getUserByUserId(userId);
        if (user == null && userId.length() == UUID_LENGTH) {
            throw new NotFoundException("User not found: " + userId);
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel) {
        return ResponseEntity.created(null).body(userService.addUser(userRequestModel));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModel> updateUser(@RequestBody UserRequestModel userRequestModel, @PathVariable String userId) {
        UserResponseModel updatedUser = userService.getUserByUserId(userId);
        if (updatedUser == null && userId.length() == UUID_LENGTH) {
            throw new NotFoundException("User not found: " + userId);
        }
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.created(null).body(userService.updateUser(userRequestModel, userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        UserResponseModel user = userService.getUserByUserId(userId);
        if (user == null && userId.length() == UUID_LENGTH) {
            throw new NotFoundException("User not found: " + userId);
        }
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}/firstname")
    public ResponseEntity<String> getUserFirstName(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.getUserFirstNameById(userId));
    }

    @GetMapping("/{userId}/lastname")
    public ResponseEntity<String> getUserLastName(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.getUserLastNameById(userId));
    }

    @GetMapping("/{userId}/dailycalorieintake")
    public ResponseEntity<Integer> getDailyCalorieIntake(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.getDailyCalorieIntakeById(userId));
    }

    @GetMapping("/{userId}/workoutdays")
    public ResponseEntity<List<String>> getWorkoutDays(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(userService.getWorkoutDaysById(userId));
    }

}
