package com.fitnesstracker.workouts.presentationlayer;

import com.fitnesstracker.workouts.dataaccesslayer.WorkoutRepository;
import com.fitnesstracker.workouts.dataaccesslayer.WorkoutType;
import com.fitnesstracker.workouts.presentationlayer.WorkoutRequestModel;
import com.fitnesstracker.workouts.presentationlayer.WorkoutResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("h2")
@Sql(scripts = {"/data-sql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WorkoutControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WorkoutRepository workoutRepository;

    private static final String BASE_URL = "/api/v1/workouts";
    private static final String VALID_WORKOUT_ID = "c27f56a7-b2c5-4e4d-93b3-6d47300d4fd7";
    private static final String NOT_FOUND_WORKOUT_ID = UUID.randomUUID().toString();
    private static final String INVALID_WORKOUT_ID = "invalid-workout-id";

    @Test
    public void whenGetWorkouts_thenReturnAllWorkouts() {
        // Given
        long expectedCount = workoutRepository.count();

        // When & Then
        webTestClient.get()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(WorkoutResponseModel.class)
                .value(workouts -> {
                    assertNotNull(workouts);
                    assertEquals(expectedCount, workouts.size());
                    workouts.forEach(this::validateWorkoutResponseModel);
                });
    }

    @Test
    public void whenGetWorkoutWithValidId_thenReturnWorkout() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(WorkoutResponseModel.class)
                .value(workout -> {
                    assertNotNull(workout);
                    assertEquals(VALID_WORKOUT_ID, workout.getWorkoutId());
                    validateWorkoutResponseModel(workout);
                });
    }

    @Test
    public void whenGetWorkoutWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{workoutId}", NOT_FOUND_WORKOUT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenGetWorkoutWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{workoutId}", INVALID_WORKOUT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void whenAddValidWorkout_thenWorkoutIsCreated() {
        // Given
        WorkoutRequestModel newWorkout = new WorkoutRequestModel(
                "Morning Run", WorkoutType.CARDIO, 30, LocalDate.now()
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newWorkout)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkoutResponseModel.class)
                .value(workout -> {
                    assertNotNull(workout);
                    assertNotNull(workout.getWorkoutId());
                    assertEquals("Morning Run", workout.getWorkoutName());
                    assertEquals(WorkoutType.CARDIO, workout.getWorkoutType());
                    assertEquals(30, workout.getDurationInMinutes());
                    assertNotNull(workout.getWorkoutDate());
                });
    }

    @Test
    public void whenUpdateWorkoutWithValidData_thenWorkoutIsUpdated() {
        // Given
        WorkoutRequestModel updatedWorkout = new WorkoutRequestModel(
                "Evening Swim", WorkoutType.AQUATIC, 45, LocalDate.now().plusDays(1)
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedWorkout)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(WorkoutResponseModel.class)
                .value(workout -> {
                    assertNotNull(workout);
                    assertEquals(VALID_WORKOUT_ID, workout.getWorkoutId());
                    assertEquals("Evening Swim", workout.getWorkoutName());
                    assertEquals(WorkoutType.AQUATIC, workout.getWorkoutType());
                    assertEquals(45, workout.getDurationInMinutes());
                });
    }

    @Test
    public void whenUpdateWorkoutWithInvalidId_thenReturnNotFound() {
        // Given
        WorkoutRequestModel updatedWorkout = new WorkoutRequestModel(
                "Non-existent", WorkoutType.STRENGTH, 60, LocalDate.now()
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{workoutId}", NOT_FOUND_WORKOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedWorkout)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteWorkoutWithValidId_thenWorkoutIsDeleted() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Verify workout is actually deleted
        webTestClient.get()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteWorkoutWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{workoutId}", NOT_FOUND_WORKOUT_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteWorkoutWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{workoutId}", INVALID_WORKOUT_ID)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    private void validateWorkoutResponseModel(WorkoutResponseModel workout) {
        assertNotNull(workout.getWorkoutId());
        assertNotNull(workout.getWorkoutName());
        assertNotNull(workout.getWorkoutType());
        assertTrue(workout.getDurationInMinutes() > 0);
        assertNotNull(workout.getWorkoutDate());
    }

    // Positive Test for Adding Workout with Valid Duration
    @Test
    public void whenAddWorkoutWithValidDuration_thenWorkoutIsCreated() {
        // Given
        WorkoutRequestModel newWorkout = new WorkoutRequestModel(
                "Valid Duration", WorkoutType.STRENGTH, 60, LocalDate.now()
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newWorkout)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WorkoutResponseModel.class)
                .value(workout -> assertEquals(60, workout.getDurationInMinutes()));
    }

    // Positive Test for Updating Workout with Valid Duration
    @Test
    public void whenUpdateWorkoutWithValidDuration_thenWorkoutIsUpdated() {
        // Given
        WorkoutRequestModel updatedWorkout = new WorkoutRequestModel(
                "Updated Duration", WorkoutType.HIGH_INTENSITY, 20, LocalDate.now()
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedWorkout)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(WorkoutResponseModel.class)
                .value(workout -> assertEquals(20, workout.getDurationInMinutes()));
    }

    // Negative Test for Adding Workout with Negative Duration
    @Test
    public void whenAddWorkoutWithNegativeDuration_thenReturnBadRequest() {
        // Given
        WorkoutRequestModel newWorkout = new WorkoutRequestModel(
                "Negative Duration", WorkoutType.FLEXIBILITY, -10, LocalDate.now()
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newWorkout)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid duration: -10");
    }

    // Negative Test for Updating Workout with Negative Duration
    @Test
    public void whenUpdateWorkoutWithNegativeDuration_thenReturnBadRequest() {
        // Given
        WorkoutRequestModel updatedWorkout = new WorkoutRequestModel(
                "Negative Update", WorkoutType.CARDIO, -5, LocalDate.now()
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{workoutId}", VALID_WORKOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedWorkout)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Invalid duration: -5");
    }
}