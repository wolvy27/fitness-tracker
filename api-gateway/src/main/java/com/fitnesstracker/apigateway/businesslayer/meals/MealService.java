package com.fitnesstracker.apigateway.businesslayer.meals;

import com.fitnesstracker.apigateway.presentationlayer.meals.MealRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.meals.MealResponseModel;
import java.util.List;

public interface MealService {
    List<MealResponseModel> getMeals();
    MealResponseModel getMealByMealId(String mealId);
    MealResponseModel addMeal(MealRequestModel mealRequestModel);
    MealResponseModel updateMeal(MealRequestModel mealRequestModel, String mealId);
    void deleteMeal(String mealId);
}