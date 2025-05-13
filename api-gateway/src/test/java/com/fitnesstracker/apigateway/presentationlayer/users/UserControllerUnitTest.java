package com.fitnesstracker.apigateway.presentationlayer.users;

import com.fitnesstracker.apigateway.businesslayer.users.UserService;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final UserResponseModel mockUser = new UserResponseModel(
            "user123", "John", "Doe", 30, 180, 75,
            "Lose weight", 2000, List.of("Monday", "Wednesday", "Friday")
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userService.getUserByUserId("user123")).thenReturn(mockUser);

        var response = userController.getUser("user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService).getUserByUserId("user123");
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserMissing() {
        String nonExistentUserId = "non-existent-id";
        when(userService.getUserByUserId(nonExistentUserId))
                .thenThrow(new NotFoundException("User not found"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            userController.getUser(nonExistentUserId);
        });

        assertEquals("User not found", ex.getMessage());
        verify(userService, times(1)).getUserByUserId(nonExistentUserId);
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userService.getUsers()).thenReturn(List.of(mockUser));

        var response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(mockUser, response.getBody().get(0));
        verify(userService).getUsers();
    }

    @Test
    void addUser_ShouldReturnCreatedUser() {
        UserRequestModel request = new UserRequestModel(
                "John", "Doe", 30, 180, 75,
                "Lose weight", 2000, List.of("Monday", "Wednesday", "Friday")
        );
        when(userService.addUser(request)).thenReturn(mockUser);

        var response = userController.addUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService).addUser(request);
    }

    @Test
    void addUser_ShouldHandleServiceError() {
        UserRequestModel request = new UserRequestModel(
                "Invalid", "", -1, -1, -1, "", -1, List.of()
        );
        when(userService.addUser(request)).thenThrow(new RuntimeException("Validation failed"));

        assertThrows(RuntimeException.class, () -> userController.addUser(request));
        verify(userService).addUser(request);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        UserRequestModel updateRequest = new UserRequestModel(
                "Jane", "Doe", 28, 170, 65,
                "Build muscle", 2200, List.of("Tuesday", "Thursday")
        );
        UserResponseModel updatedUser = new UserResponseModel(
                "user123", "Jane", "Doe", 28, 170, 65,
                "Build muscle", 2200, List.of("Tuesday", "Thursday")
        );

        when(userService.updateUser(updateRequest, "user123")).thenReturn(updatedUser);

        var response = userController.updateUser("user123", updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(userService).updateUser(updateRequest, "user123");
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserMissing() {
        String nonExistentUserId = "non-existent-id";
        UserRequestModel updateRequest = new UserRequestModel(
                "Jane", "Doe", 28, 170, 65,
                "Build muscle", 2200, List.of("Tuesday", "Thursday")
        );

        when(userService.updateUser(updateRequest, nonExistentUserId))
                .thenThrow(new NotFoundException("User not found"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            userController.updateUser(nonExistentUserId, updateRequest);
        });

        assertEquals("User not found", ex.getMessage());
        verify(userService, times(1)).updateUser(updateRequest, nonExistentUserId);
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).deleteUser("user123");

        var response = userController.deleteUser("user123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUser("user123");
    }

    @Test
    void deleteUser_ShouldHandleServiceException() {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser("bad_id");

        assertThrows(RuntimeException.class, () -> userController.deleteUser("bad_id"));
        verify(userService).deleteUser("bad_id");
    }
}
