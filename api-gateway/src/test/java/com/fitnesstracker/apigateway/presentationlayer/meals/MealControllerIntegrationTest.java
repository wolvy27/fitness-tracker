package com.fitnesstracker.apigateway.presentationlayer.meals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.businesslayer.meals.MealService;
import com.fitnesstracker.apigateway.domainclientlayer.meals.MealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
public class MealControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    private MealRequestModel mealRequest;
    private MealResponseModel mealResponse;

    @BeforeEach
    void setUp() {
        mealRequest = MealRequestModel.builder()
                .mealName("Oatmeal with Banana")
                .calories(350)
                .mealDate(LocalDate.of(2025, 5, 12))
                .mealType(MealType.BREAKFAST)
                .build();

        mealResponse = MealResponseModel.builder()
                .mealId("meal123")
                .mealName("Oatmeal with Banana")
                .calories(350)
                .mealDate(LocalDate.of(2025, 5, 12))
                .mealType(MealType.BREAKFAST)
                .build();
    }

    @Test
    void getMeals_shouldReturnListOfMeals() throws Exception {
        List<MealResponseModel> meals = Collections.singletonList(mealResponse);
        Mockito.when(mealService.getMeals()).thenReturn(meals);

        mockMvc.perform(get("/api/v1/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].mealId", is("meal123")))
                .andExpect(jsonPath("$[0].mealName", is("Oatmeal with Banana")));
    }

    @Test
    void getMealById_shouldReturnMeal() throws Exception {
        Mockito.when(mealService.getMealByMealId("meal123")).thenReturn(mealResponse);

        mockMvc.perform(get("/api/v1/meals/meal123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealId", is("meal123")))
                .andExpect(jsonPath("$.mealName", is("Oatmeal with Banana")))
                .andExpect(jsonPath("$.mealType", is("BREAKFAST")));
    }

    @Test
    void addMeal_shouldReturnCreatedMeal() throws Exception {
        Mockito.when(mealService.addMeal(any(MealRequestModel.class))).thenReturn(mealResponse);

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mealId", is("meal123")))
                .andExpect(jsonPath("$.mealName", is("Oatmeal with Banana")));
    }

    @Test
    void updateMeal_shouldReturnUpdatedMeal() throws Exception {
        MealRequestModel updatedRequest = MealRequestModel.builder()
                .mealName("Updated Meal")
                .calories(400)
                .mealDate(LocalDate.of(2025, 5, 13))
                .mealType(MealType.LUNCH)
                .build();

        MealResponseModel updatedResponse = MealResponseModel.builder()
                .mealId("meal123")
                .mealName("Updated Meal")
                .calories(400)
                .mealDate(LocalDate.of(2025, 5, 13))
                .mealType(MealType.LUNCH)
                .build();

        Mockito.when(mealService.updateMeal(eq(updatedRequest), eq("meal123"))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/meals/meal123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mealName", is("Updated Meal")))
                .andExpect(jsonPath("$.mealType", is("LUNCH")));
    }

    @Test
    void deleteMeal_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/meals/meal123"))
                .andExpect(status().isNoContent());
    }
}
