package com.fitnesstracker.dailylogs.presentationlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitnesstracker.dailylogs.dataaccesslayer.DailyLogRepository;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DailyLogControllerIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DailyLogRepository dailyLogRepository;

    private MockRestServiceServer mockRestServiceServer;

    private ObjectMapper objectMapper;

    private final String FOUND_USER_ID = "2dbdcb96-6479-430b-860f-bf99fd854940"; // Alice
    private final String NOT_FOUND_USER_ID = UUID.randomUUID().toString();

    private final String BASE_URI_USERS = "http://localhost:8080/api/v1/users/";
    private final String BASE_URI_WORKOUTS = "http://localhost:8081/api/v1/workouts/";
    private final String BASE_URI_MEALS = "http://localhost:8082/api/v1/meals/";

    @BeforeEach
    public void init() {
        // Create a new ObjectMapper with proper configuration
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Reset and recreate MockRestServiceServer before each test
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();

        assertTrue(dailyLogRepository.count() > 0, "Test database should have pre-populated logs");
    }

    @Test
    public void whenUserIdExists_thenReturnAllDailyLogs() throws URISyntaxException, JsonProcessingException {
        // arrange
        UserModel userModel = UserModel.builder()
                .userId(FOUND_USER_ID)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        // mock the get request to users service
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(userModel)));

        // mock workout days
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/workoutdays")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(List.of("MONDAY", "WEDNESDAY", "FRIDAY"))));

        // mock daily calorie intake
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/dailycalorieintake")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1800"));

        // act & assert
        webTestClient.get()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DailyLogResponseModel.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertTrue(list.size() > 0);
                    assertEquals(FOUND_USER_ID, list.get(0).getUserIdentifier());
                    assertEquals("Alice", list.get(0).getUserFirstName());
                    assertEquals("Johnson", list.get(0).getUserLastName());
                });
    }

    @Test
    public void whenGetDailyLogByIdWithValidIds_thenReturnDailyLog() throws URISyntaxException, JsonProcessingException {
        // arrange
        String existingDailyLogId = "d1a1b1c1-d1e1-41f1-g1h1-i1j1k1l1m1n1"; // Alice's log

        UserModel userModel = UserModel.builder()
                .userId(FOUND_USER_ID)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        // mock the get request to users service
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(userModel)));

        // mock workout days
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/workoutdays")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(List.of("MONDAY", "WEDNESDAY", "FRIDAY"))));

        // mock daily calorie intake
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/dailycalorieintake")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1800"));

        // act & assert
        webTestClient.get()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs/" + existingDailyLogId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DailyLogResponseModel.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(existingDailyLogId, response.getDailyLogIdentifier());
                    assertEquals(FOUND_USER_ID, response.getUserIdentifier());
                    assertEquals("Alice", response.getUserFirstName());
                    assertEquals("Johnson", response.getUserLastName());
                    assertEquals("Cardio", response.getWorkoutName());
                    assertEquals(45, response.getWorkoutDurationInMinutes());
                    assertEquals(350, response.getBreakfastCalories());
                    assertEquals(450, response.getLunchCalories());
                });
    }

    @Test
    public void whenValidRequest_thenCreateDailyLog() throws URISyntaxException, JsonProcessingException {
        // arrange
        UserModel userModel = UserModel.builder()
                .userId(FOUND_USER_ID)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        WorkoutModel workoutModel = WorkoutModel.builder()
                .workoutId("38f8492b-4d5a-4326-9981-32bffb132938")
                .workoutName("Cardio")
                .workoutDurationInMinutes(45)
                .build();

        MealModel breakfastModel = MealModel.builder()
                .mealId("a1b2c3d4-e5f6-47g8-h9i0-j1k2l3m4n5o6")
                .mealName("Overnight Oats")
                .mealCalorie(350)
                .build();

        // Mock external service calls
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(userModel)));

        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_WORKOUTS + workoutModel.getWorkoutId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(workoutModel)));

        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_MEALS + breakfastModel.getMealId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(breakfastModel)));

        // Mock workout days
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/workoutdays")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(List.of("MONDAY", "WEDNESDAY", "FRIDAY"))));

        // Mock daily calorie intake
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/dailycalorieintake")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1800"));

        // Create request
        DailyLogRequestModel requestModel = DailyLogRequestModel.builder()
                .workoutIdentifier(workoutModel.getWorkoutId())
                .logDate(LocalDate.of(2025, 3, 17)) // New date not in test data
                .breakfastIdentifier(breakfastModel.getMealId())
                .build();

        // act & assert
        webTestClient.post()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DailyLogResponseModel.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.getDailyLogIdentifier());
                    assertEquals(FOUND_USER_ID, response.getUserIdentifier());
                    assertEquals("Alice", response.getUserFirstName());
                    assertEquals("Johnson", response.getUserLastName());
                    assertEquals("Cardio", response.getWorkoutName());
                    assertEquals(45, response.getWorkoutDurationInMinutes());
                    assertEquals("Overnight Oats", response.getBreakfastName());
                    assertEquals(350, response.getBreakfastCalories());
                });
    }

    @Test
    @DirtiesContext
    public void whenUpdateDailyLogWithValidData_thenReturnUpdatedDailyLog() throws URISyntaxException, JsonProcessingException {
        // arrange
        String existingDailyLogId = "d1a1b1c1-d1e1-41f1-g1h1-i1j1k1l1m1n1"; // Alice's log

        UserModel userModel = UserModel.builder()
                .userId(FOUND_USER_ID)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        WorkoutModel workoutModel = WorkoutModel.builder()
                .workoutId("38f8492b-4d5a-4326-9981-32bffb132938")
                .workoutName("Updated Cardio")
                .workoutDurationInMinutes(60)
                .build();

        // Mock external service calls
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(userModel)));

        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_WORKOUTS + workoutModel.getWorkoutId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(workoutModel)));

        // Mock workout days
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/workoutdays")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(List.of("MONDAY", "WEDNESDAY", "FRIDAY"))));

        // Mock daily calorie intake
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID + "/dailycalorieintake")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1800"));

        // Create request
        DailyLogRequestModel requestModel = DailyLogRequestModel.builder()
                .workoutIdentifier(workoutModel.getWorkoutId())
                .logDate(LocalDate.of(2025, 3, 10)) // Existing date
                .build();

        // act & assert
        webTestClient.put()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs/" + existingDailyLogId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DailyLogResponseModel.class)
                .value(response -> {
                    assertNotNull(response);
                    assertEquals(existingDailyLogId, response.getDailyLogIdentifier());
                    assertEquals("Updated Cardio", response.getWorkoutName());
                    assertEquals(60, response.getWorkoutDurationInMinutes());
                });
    }

    @Test
    public void whenDeleteDailyLogWithValidIds_thenReturnNoContent() throws URISyntaxException, JsonProcessingException {
        // arrange
        String existingDailyLogId = "d1a1b1c1-d1e1-41f1-g1h1-i1j1k1l1m1n1"; // Alice's log

        UserModel userModel = UserModel.builder()
                .userId(FOUND_USER_ID)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        // mock the get request to users service
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(userModel)));

        // act & assert
        webTestClient.delete()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs/" + existingDailyLogId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenUserIdDoesNotExist_thenReturnNotFound() throws URISyntaxException {
        // arrange
        mockRestServiceServer.expect(ExpectedCount.manyTimes(),
                        requestTo(new URI(BASE_URI_USERS + NOT_FOUND_USER_ID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // act & assert
        webTestClient.get()
                .uri("/api/v1/" + NOT_FOUND_USER_ID + "/dailyLogs")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenInvalidDateFormat_thenReturnBadRequest() throws Exception {
        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/" + FOUND_USER_ID + "/dailyLogs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"logDate\": \"2025/03/17\"}") // Invalid format
                .exchange()
                .expectStatus().isBadRequest();
    }


}