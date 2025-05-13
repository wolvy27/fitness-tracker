package com.fitnesstracker.apigateway.presentationlayer.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.businesslayer.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseModel sampleUserResponse;
    private UserRequestModel sampleUserRequest;

    @BeforeEach
    void setUp() {
        sampleUserResponse = new UserResponseModel(
                "user123", "John", "Doe", 30, 180, 75,
                "Lose weight", 2000, List.of("Monday", "Wednesday", "Friday"));

        sampleUserRequest = new UserRequestModel(
                "John", "Doe", 30, 180, 75,
                "Lose weight", 2000, List.of("Monday", "Wednesday", "Friday"));
    }

    @Test
    void whenGetUserById_thenReturnUser() throws Exception {
        Mockito.when(userService.getUserByUserId("user123"))
                .thenReturn(sampleUserResponse);

        mockMvc.perform(get("/api/v1/users/user123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void whenGetUsers_thenReturnUserList() throws Exception {
        Mockito.when(userService.getUsers())
                .thenReturn(singletonList(sampleUserResponse));

        mockMvc.perform(get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }

    @Test
    void whenAddUser_thenReturnCreatedUser() throws Exception {
        Mockito.when(userService.addUser(any(UserRequestModel.class)))
                .thenReturn(sampleUserResponse);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        Mockito.when(userService.updateUser(any(UserRequestModel.class), eq("user123")))
                .thenReturn(sampleUserResponse);

        mockMvc.perform(put("/api/v1/users/user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    void whenDeleteUser_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/users/user123"))
                .andExpect(status().isNoContent());
    }
}
