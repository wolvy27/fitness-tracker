package com.fitnesstracker.users.presentationlayer;


import com.fitnesstracker.users.dataaccesslayer.UserRepository;
import com.fitnesstracker.users.presentationlayer.UserRequestModel;
import com.fitnesstracker.users.presentationlayer.UserResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("h2")
@Sql(scripts = {"/data-sql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private static final String BASE_URL = "/api/v1/users";
    private static final String VALID_USER_ID = "2dbdcb96-6479-430b-860f-bf99fd854940";
    private static final String NOT_FOUND_USER_ID = UUID.randomUUID().toString();
    private static final String INVALID_USER_ID = "invalid-user-id";

    // HAL+JSON media type for HATEOAS responses
    private static final String HAL_JSON_MEDIA_TYPE = "application/hal+json";

    @Test
    public void whenGetUsers_thenReturnAllUsers() {
        // Given
        long expectedCount = userRepository.count();

        // When & Then
        webTestClient.get()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseModel.class)
                .value(users -> {
                    assertNotNull(users);
                    assertEquals(expectedCount, users.size());
                    users.forEach(this::validateUserResponseModel);
                });
    }

    @Test
    public void whenGetUserWithValidId_thenReturnUser() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponseModel.class)
                .value(user -> {
                    assertNotNull(user);
                    assertEquals(VALID_USER_ID, user.getUserId());
                    validateUserResponseModel(user);
                });
    }

    @Test
    public void whenGetUserWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{userId}", NOT_FOUND_USER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenGetUserWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{userId}", INVALID_USER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    @Test
    public void whenAddValidUser_thenUserIsCreated() {
        // Given
        UserRequestModel newUser = new UserRequestModel(
                "New", "User", 30, 175, 70,
                "Maintain fitness", 2200, List.of("MONDAY", "WEDNESDAY", "FRIDAY")
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(HAL_JSON_MEDIA_TYPE)
                .expectBody(UserResponseModel.class)
                .value(user -> {
                    assertNotNull(user);
                    assertNotNull(user.getUserId());
                    assertEquals("New", user.getFirstName());
                    assertEquals("User", user.getLastName());
                    assertEquals(30, user.getAge());
                    assertEquals(175, user.getHeightInCm());
                    assertEquals(70, user.getWeightInKg());
                    assertEquals("Maintain fitness", user.getGoalDescription());
                    assertEquals(2200, user.getDailyCaloricIntake());
                    assertEquals(3, user.getWorkoutDays().size());
                });
    }

    @Test
    public void whenUpdateUserWithValidData_thenUserIsUpdated() {
        // Given
        UserRequestModel updatedUser = new UserRequestModel(
                "Updated", "Name", 35, 180, 75,
                "New goal", 2500, List.of("TUESDAY", "THURSDAY")
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isCreated() // Changed from isOk to isCreated
                .expectHeader().contentType(HAL_JSON_MEDIA_TYPE)
                .expectBody(UserResponseModel.class)
                .value(user -> {
                    assertNotNull(user);
                    assertEquals(VALID_USER_ID, user.getUserId());
                    assertEquals("Updated", user.getFirstName());
                    assertEquals("Name", user.getLastName());
                    assertEquals(35, user.getAge());
                    assertEquals(180, user.getHeightInCm());
                    assertEquals(75, user.getWeightInKg());
                    assertEquals("New goal", user.getGoalDescription());
                    assertEquals(2500, user.getDailyCaloricIntake());
                    assertEquals(2, user.getWorkoutDays().size());
                });
    }

    @Test
    public void whenUpdateUserWithInvalidId_thenReturnNotFound() {
        // Given
        UserRequestModel updatedUser = new UserRequestModel(
                "Updated", "Name", 35, 180, 75,
                "New goal", 2500, List.of("TUESDAY", "THURSDAY")
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{userId}", "non-existent-id")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteUserWithValidId_thenUserIsDeleted() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .exchange()
                .expectStatus().isNoContent();

        // Verify user is actually deleted
        webTestClient.get()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteUserWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{userId}", NOT_FOUND_USER_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteUserWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{userId}", INVALID_USER_ID)
                .exchange()
                .expectStatus().isEqualTo(422);
    }

    private void validateUserResponseModel(UserResponseModel user) {
        assertNotNull(user.getUserId());
        assertNotNull(user.getFirstName());
        assertNotNull(user.getLastName());
        assertTrue(user.getAge() > 0);
        assertTrue(user.getHeightInCm() > 0);
        assertTrue(user.getWeightInKg() > 0);
        assertNotNull(user.getGoalDescription());
        assertTrue(user.getDailyCaloricIntake() > 0);
        assertNotNull(user.getWorkoutDays());
    }

    // Positive Test for Adding User with Valid Caloric Intake
    @Test
    public void whenAddUserWithValidCaloricIntake_thenUserIsCreated() {
        // Given
        UserRequestModel newUser = new UserRequestModel(
                "Positive", "Calories", 25, 160, 60,
                "Gain muscle", 3000, List.of("MONDAY", "WEDNESDAY")
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseModel.class)
                .value(user -> assertEquals(3000, user.getDailyCaloricIntake()));
    }

    // Positive Test for Updating User with Valid Caloric Intake
    @Test
    public void whenUpdateUserWithValidCaloricIntake_thenUserIsUpdated() {
        // Given
        UserRequestModel updatedUser = new UserRequestModel(
                "Updated", "Calories", 32, 170, 70,
                "Maintain", 2800, List.of("TUESDAY", "THURSDAY")
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseModel.class)
                .value(user -> assertEquals(2800, user.getDailyCaloricIntake()));
    }

    // Negative Test for Adding User with Negative Caloric Intake
    @Test
    public void whenAddUserWithNegativeCaloricIntake_thenReturnBadRequest() {
        // Given
        UserRequestModel newUser = new UserRequestModel(
                "Negative", "Calories", 40, 180, 80,
                "Lose weight", -500, List.of("FRIDAY", "SUNDAY")
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Daily caloric intake cannot be negative.");
    }

    // Negative Test for Updating User with Negative Caloric Intake
    @Test
    public void whenUpdateUserWithNegativeCaloricIntake_thenReturnBadRequest() {
        // Given
        UserRequestModel updatedUser = new UserRequestModel(
                "Negative", "Update", 45, 175, 90,
                "New plan", -100, List.of("MONDAY", "SATURDAY")
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{userId}", VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Daily caloric intake cannot be negative.");
    }
}