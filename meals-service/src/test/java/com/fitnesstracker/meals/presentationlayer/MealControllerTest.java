package com.fitnesstracker.meals.presentationlayer;

import com.fitnesstracker.meals.dataaccesslayer.MealRepository;
import com.fitnesstracker.meals.dataaccesslayer.MealType;
import com.fitnesstracker.meals.presentationlayer.MealRequestModel;
import com.fitnesstracker.meals.presentationlayer.MealResponseModel;
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
@Sql(scripts = {"/data-psql.sql"})  // You might need to adjust this path
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MealControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MealRepository mealRepository;

    private static final String BASE_URL = "/api/v1/meals";  // Adjust URL as needed
    private static final String VALID_MEAL_ID = "e5f6g7h8-i9j0-81k2-l3m4-n5o6p7q8r9s0";  // Replace with a valid ID from your test data
    private static final String NOT_FOUND_MEAL_ID = UUID.randomUUID().toString();
    private static final String INVALID_MEAL_ID = "invalid-meal-id-format";

    private static final String HAL_JSON_MEDIA_TYPE = "application/hal+json";

    @Test
    public void whenGetMeals_thenReturnAllMeals() {
        // Given
        long expectedCount = mealRepository.count();

        // When & Then
        webTestClient.get()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MealResponseModel.class)
                .value(meals -> {
                    assertNotNull(meals);
                    assertEquals(expectedCount, meals.size());
                    meals.forEach(this::validateMealResponseModel);
                });
    }

    @Test
    public void whenGetMealWithValidId_thenReturnMeal() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{mealId}", VALID_MEAL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MealResponseModel.class)
                .value(meal -> {
                    assertNotNull(meal);
                    assertEquals(VALID_MEAL_ID, meal.getMealId());
                    validateMealResponseModel(meal);
                });
    }

    @Test
    public void whenGetMealWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{mealId}", NOT_FOUND_MEAL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenGetMealWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/{mealId}", INVALID_MEAL_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(422);  // Or appropriate status code for invalid format
    }

    @Test
    public void whenAddValidMeal_thenMealIsCreated() {
        // Given
        MealRequestModel newMeal = new MealRequestModel(
                "Test Meal",
                500,
                LocalDate.now(),
                MealType.LUNCH
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newMeal)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(HAL_JSON_MEDIA_TYPE)
                .expectBody(MealResponseModel.class)
                .value(meal -> {
                    assertNotNull(meal);
                    assertNotNull(meal.getMealId());
                    assertEquals("Test Meal", meal.getMealName());
                    assertEquals(500, meal.getCalories());
                    assertEquals(LocalDate.now(), meal.getMealDate());
                    assertEquals(MealType.LUNCH, meal.getMealType());
                });
    }

    @Test
    public void whenUpdateMealWithValidData_thenMealIsUpdated() {
        // Given
        MealRequestModel updatedMeal = new MealRequestModel(
                "Updated Meal",
                600,
                LocalDate.now().plusDays(1),
                MealType.DINNER
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{mealId}", VALID_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedMeal)
                .exchange()
                .expectStatus().isCreated()  // Assuming PUT returns Created
                .expectHeader().contentType(HAL_JSON_MEDIA_TYPE)
                .expectBody(MealResponseModel.class)
                .value(meal -> {
                    assertNotNull(meal);
                    assertEquals(VALID_MEAL_ID, meal.getMealId());
                    assertEquals("Updated Meal", meal.getMealName());
                    assertEquals(600, meal.getCalories());
                    assertEquals(LocalDate.now().plusDays(1), meal.getMealDate());
                    assertEquals(MealType.DINNER, meal.getMealType());
                });
    }

    @Test
    public void whenUpdateMealWithInvalidId_thenReturnNotFound() {
        // Given
        MealRequestModel updatedMeal = new MealRequestModel(
                "Updated Meal",
                600,
                LocalDate.now().plusDays(1),
                MealType.DINNER
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{mealId}", NOT_FOUND_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedMeal)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteMealWithValidId_thenMealIsDeleted() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{mealId}", VALID_MEAL_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenDeleteMealWithInvalidId_thenReturnNotFound() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{mealId}", NOT_FOUND_MEAL_ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void whenDeleteMealWithInvalidIdFormat_thenReturnUnprocessableEntity() {
        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/{mealId}", INVALID_MEAL_ID)
                .exchange()
                .expectStatus().isEqualTo(422);  // Or the appropriate error code
    }

    private void validateMealResponseModel(MealResponseModel meal) {
        assertNotNull(meal.getMealId());
        assertNotNull(meal.getMealName());
        assertNotNull(meal.getCalories());
        assertNotNull(meal.getMealDate());
        assertNotNull(meal.getMealType());
    }

    // Additional Tests

    @Test
    public void whenAddMealWithValidCalories_thenMealIsCreated() {
        // Given
        MealRequestModel newMeal = new MealRequestModel(
                "Valid Calories",
                800,
                LocalDate.now(),
                MealType.BREAKFAST
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newMeal)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MealResponseModel.class)
                .value(meal -> assertEquals(800, meal.getCalories()));
    }

    @Test
    public void whenUpdateMealWithValidCalories_thenMealIsUpdated() {
        // Given
        MealRequestModel updatedMeal = new MealRequestModel(
                "Updated Calories",
                900,
                LocalDate.now().plusDays(1),
                MealType.SNACK
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{mealId}", VALID_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedMeal)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MealResponseModel.class)
                .value(meal -> assertEquals(900, meal.getCalories()));
    }

    @Test
    public void whenAddMealWithInvalidCalories_thenReturnBadRequest() {  // Or appropriate status
        // Given
        MealRequestModel newMeal = new MealRequestModel(
                "Invalid Calories",
                -100,
                LocalDate.now(),
                MealType.DINNER
        );

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newMeal)
                .exchange()
                .expectStatus().isBadRequest();  // Or appropriate status
        //.expectBody()
        //.jsonPath("$.message").isEqualTo("Calories cannot be negative"); //Adapt the message as needed
    }

    @Test
    public void whenUpdateMealWithInvalidCalories_thenReturnBadRequest() { // Or appropriate status
        // Given
        MealRequestModel updatedMeal = new MealRequestModel(
                "Invalid Update Calories",
                -200,
                LocalDate.now().plusDays(1),
                MealType.LUNCH
        );

        // When & Then
        webTestClient.put()
                .uri(BASE_URL + "/{mealId}", VALID_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedMeal)
                .exchange()
                .expectStatus().isBadRequest();   // Or appropriate status
    }
}