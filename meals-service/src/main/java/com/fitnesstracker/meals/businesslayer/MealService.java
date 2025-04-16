package com.fitnesstracker.meals.businesslayer;

import com.fitnesstracker.meals.dataaccesslayer.MealType;
import com.fitnesstracker.meals.presentationlayer.MealRequestModel;
import com.fitnesstracker.meals.presentationlayer.MealResponseModel;

import java.util.List;

public interface MealService {
    List<MealResponseModel> getMeals();
    MealResponseModel getMealByMealId(String mealId);
    MealResponseModel addMeal(MealRequestModel newMealData);
    MealResponseModel updateMeal(MealRequestModel newMealData, String mealId);
    void deleteMeal(String mealId);

    // For DailyLog aggregate
    //String getMealNameByMealId(String mealId, String userId);
   // Integer getCaloriesByMealId(String mealId, String userId);
    //MealType getMealTypeByMealId(String mealId);
}
