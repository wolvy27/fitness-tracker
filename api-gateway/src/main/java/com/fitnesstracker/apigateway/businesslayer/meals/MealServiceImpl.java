package com.fitnesstracker.apigateway.businesslayer.meals;

import com.fitnesstracker.apigateway.domainclientlayer.meals.MealServiceClient;
import com.fitnesstracker.apigateway.presentationlayer.meals.MealRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.meals.MealResponseModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {
    private final MealServiceClient mealServiceClient;

    public MealServiceImpl(MealServiceClient mealServiceClient) {
        this.mealServiceClient = mealServiceClient;
    }

    @Override
    public List<MealResponseModel> getMeals() {
        return mealServiceClient.getMeals();
    }

    @Override
    public MealResponseModel getMealByMealId(String mealId) {
        return mealServiceClient.getMealByMealId(mealId);
    }

    @Override
    public MealResponseModel addMeal(MealRequestModel mealRequestModel) {
        return mealServiceClient.addMeal(mealRequestModel);
    }

    @Override
    public MealResponseModel updateMeal(MealRequestModel mealRequestModel, String mealId) {
        return mealServiceClient.updateMeal(mealRequestModel, mealId);
    }

    @Override
    public void deleteMeal(String mealId) {
        mealServiceClient.deleteMeal(mealId);
    }
}