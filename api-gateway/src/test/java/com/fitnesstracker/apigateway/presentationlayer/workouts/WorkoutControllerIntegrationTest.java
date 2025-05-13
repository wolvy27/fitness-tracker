package com.fitnesstracker.apigateway.presentationlayer.workouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.domainclientlayer.workouts.WorkoutType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenPostWorkout_thenCreatedAndReturnWorkout() throws Exception {
        WorkoutRequestModel workout = new WorkoutRequestModel(
                "Morning Cardio",
                WorkoutType.CARDIO,
                45,
                LocalDate.now()
        );

        mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workoutId").exists())
                .andExpect(jsonPath("$.workoutName").value("Morning Cardio"))
                .andExpect(jsonPath("$.workoutType").value("CARDIO"))
                .andExpect(jsonPath("$.durationInMinutes").value(45))
                .andExpect(jsonPath("$.workoutDate").value(LocalDate.now().toString()));
    }

    @Test
    void whenGetWorkouts_thenReturnWorkoutList() throws Exception {
        mockMvc.perform(get("/api/v1/workouts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(List.class)));
    }

    @Test
    void whenGetWorkoutById_thenReturnWorkout() throws Exception {
        // First create one
        WorkoutRequestModel workout = new WorkoutRequestModel(
                "Strength Training",
                WorkoutType.STRENGTH,
                60,
                LocalDate.now()
        );

        String response = mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkoutResponseModel createdWorkout = objectMapper.readValue(response, WorkoutResponseModel.class);

        // Then retrieve it
        mockMvc.perform(get("/api/v1/workouts/" + createdWorkout.getWorkoutId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutId").value(createdWorkout.getWorkoutId()))
                .andExpect(jsonPath("$.workoutName").value("Strength Training"));
    }

    @Test
    void whenPutWorkout_thenUpdateWorkout() throws Exception {
        // First create one
        WorkoutRequestModel workout = new WorkoutRequestModel(
                "Evening Run",
                WorkoutType.CARDIO,
                30,
                LocalDate.now()
        );

        String response = mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkoutResponseModel createdWorkout = objectMapper.readValue(response, WorkoutResponseModel.class);

        // Update it
        WorkoutRequestModel updatedWorkout = new WorkoutRequestModel(
                "Evening HIIT",
                WorkoutType.HIGH_INTENSITY,
                35,
                LocalDate.now()
        );

        mockMvc.perform(put("/api/v1/workouts/" + createdWorkout.getWorkoutId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedWorkout)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workoutName").value("Evening HIIT"))
                .andExpect(jsonPath("$.workoutType").value("HIGH_INTENSITY"))
                .andExpect(jsonPath("$.durationInMinutes").value(35));
    }

    @Test
    void whenDeleteWorkout_thenNoContent() throws Exception {
        // First create one
        WorkoutRequestModel workout = new WorkoutRequestModel(
                "Temporary Workout",
                WorkoutType.STRENGTH,
                20,
                LocalDate.now()
        );

        String response = mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkoutResponseModel createdWorkout = objectMapper.readValue(response, WorkoutResponseModel.class);

        // Delete it
        mockMvc.perform(delete("/api/v1/workouts/" + createdWorkout.getWorkoutId()))
                .andExpect(status().isNoContent());
    }
}
