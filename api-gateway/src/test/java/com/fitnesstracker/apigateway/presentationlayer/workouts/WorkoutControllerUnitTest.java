package com.fitnesstracker.apigateway.presentationlayer.workouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.businesslayer.workouts.WorkoutService;
import com.fitnesstracker.apigateway.domainclientlayer.workouts.WorkoutType;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
class WorkoutControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkoutService workoutService;

    @Autowired
    private ObjectMapper objectMapper;

    private WorkoutResponseModel mockWorkout;
    private WorkoutRequestModel mockRequest;

    @BeforeEach
    void setUp() {
        mockWorkout = new WorkoutResponseModel(
                UUID.randomUUID().toString(),
                "Cardio Blast",
                WorkoutType.CARDIO,
                45,
                LocalDate.of(2025, 3, 10)
        );

        mockRequest = new WorkoutRequestModel(
                "Cardio Blast",
                WorkoutType.CARDIO,
                45,
                LocalDate.of(2025, 3, 10)
        );
    }

    @Test
    void getWorkoutById_shouldReturnWorkout() throws Exception {
        Mockito.when(workoutService.getWorkoutByWorkoutId(eq(mockWorkout.getWorkoutId())))
                .thenReturn(mockWorkout);

        mockMvc.perform(get("/api/v1/workouts/{workoutId}", mockWorkout.getWorkoutId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutName").value("Cardio Blast"));
    }

    @Test
    void getAllWorkouts_shouldReturnList() throws Exception {
        Mockito.when(workoutService.getWorkouts()).thenReturn(List.of(mockWorkout));

        mockMvc.perform(get("/api/v1/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void addWorkout_shouldReturnCreatedWorkout() throws Exception {
        Mockito.when(workoutService.addWorkout(any())).thenReturn(mockWorkout);

        mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.workoutName").value("Cardio Blast"));
    }

    @Test
    void addWorkout_invalidInput_shouldReturn422() throws Exception {
        // Mocking an invalid input exception that will be thrown by the service
        Mockito.when(workoutService.addWorkout(any()))
                .thenThrow(new InvalidInputException("Invalid input"));

        mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isUnprocessableEntity()) // 422
                .andExpect(jsonPath("$.message").value("Invalid input"));
    }

    @Test
    void updateWorkout_shouldReturnUpdatedWorkout() throws Exception {
        Mockito.when(workoutService.updateWorkout(any(), eq(mockWorkout.getWorkoutId())))
                .thenReturn(mockWorkout);

        mockMvc.perform(put("/api/v1/workouts/{workoutId}", mockWorkout.getWorkoutId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutName").value("Cardio Blast"));
    }

    @Test
    void updateWorkout_invalidInput_shouldReturn422() throws Exception {
        // Mocking an invalid input exception for update
        Mockito.when(workoutService.updateWorkout(any(), eq(mockWorkout.getWorkoutId())))
                .thenThrow(new InvalidInputException("Invalid input"));

        mockMvc.perform(put("/api/v1/workouts/{workoutId}", mockWorkout.getWorkoutId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isUnprocessableEntity()) // 422
                .andExpect(jsonPath("$.message").value("Invalid input"));
    }

    @Test
    void deleteWorkout_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(workoutService).deleteWorkout(eq(mockWorkout.getWorkoutId()));

        mockMvc.perform(delete("/api/v1/workouts/{workoutId}", mockWorkout.getWorkoutId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWorkout_notFound_shouldReturn404() throws Exception {
        // Simulate the scenario where the workout does not exist
        Mockito.doThrow(new NotFoundException("Workout not found"))
                .when(workoutService).deleteWorkout(eq(mockWorkout.getWorkoutId()));

        mockMvc.perform(delete("/api/v1/workouts/{workoutId}", mockWorkout.getWorkoutId()))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.message").value("Workout not found"));
    }
}
