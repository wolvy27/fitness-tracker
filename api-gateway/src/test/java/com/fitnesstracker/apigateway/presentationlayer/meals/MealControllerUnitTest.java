package com.fitnesstracker.apigateway.presentationlayer.meals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.businesslayer.meals.MealService;
import com.fitnesstracker.apigateway.domainclientlayer.meals.MealType;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
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

@WebMvcTest(MealController.class)
public class MealControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    private MealRequestModel mockRequest;
    private MealResponseModel mockResponse;

    @BeforeEach
    void setup() {
        String mealId = UUID.randomUUID().toString();
        mockRequest = new MealRequestModel(
                "Breakfast Bowl",
                350,
                LocalDate.of(2025, 5, 1),
                MealType.BREAKFAST
        );

        mockResponse = new MealResponseModel(
                mealId,
                "Breakfast Bowl",
                350,
                LocalDate.of(2025, 5, 1),
                MealType.BREAKFAST
        );
    }

    @Test
    void getMealById_shouldReturnMeal() throws Exception {
        Mockito.when(mealService.getMealByMealId(eq(mockResponse.getMealId())))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/meals/{mealId}", mockResponse.getMealId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealName").value("Breakfast Bowl"));
    }

    @Test
    void getAllMeals_shouldReturnList() throws Exception {
        Mockito.when(mealService.getMeals()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/v1/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void addMeal_shouldReturnCreatedMeal() throws Exception {
        Mockito.when(mealService.addMeal(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mealName").value("Breakfast Bowl"));
    }

    @Test
    void updateMeal_shouldReturnUpdatedMeal() throws Exception {
        Mockito.when(mealService.updateMeal(any(), eq(mockResponse.getMealId())))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/meals/{mealId}", mockResponse.getMealId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealName").value("Breakfast Bowl"));
    }

    @Test
    void deleteMeal_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(mealService).deleteMeal(eq(mockResponse.getMealId()));

        mockMvc.perform(delete("/api/v1/meals/{mealId}", mockResponse.getMealId()))
                .andExpect(status().isNoContent());
    }

    // NEGATIVE PATH – Invalid Input (e.g., malformed UUID or business rule violation)
    @Test
    void getMealById_invalidInput_shouldReturn422() throws Exception {
        String invalidMealId = "!!!INVALID_UUID###";

        Mockito.when(mealService.getMealByMealId(eq(invalidMealId)))
                .thenThrow(new InvalidInputException("Invalid meal ID format"));

        mockMvc.perform(get("/api/v1/meals/{mealId}", invalidMealId))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Invalid meal ID format"));
    }

    @Test
    void addMeal_invalidData_shouldReturn422() throws Exception {
        MealRequestModel invalidRequest = new MealRequestModel(
                "", // Empty meal name (assuming it's invalid)
                -100, // Invalid negative calories
                null, // Missing date
                null // Missing type
        );

        Mockito.when(mealService.addMeal(any()))
                .thenThrow(new InvalidInputException("Invalid meal data"));

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Invalid meal data"));
    }
}
