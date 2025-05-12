package com.fitnesstracker.dailylogs.domainclientlayer.meals;

import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MealsServiceClient {
    private final RestTemplate restTemplate;
    private final String MEALS_SERVICE_URL;

    public MealsServiceClient(RestTemplate restTemplate,
                              @Value("${app.meals-service.host}") String mealsServiceHost,
                              @Value("${app.meals-service.port}") String mealsServicePort) {
        this.restTemplate = restTemplate;
        this.MEALS_SERVICE_URL = "http://" + mealsServiceHost + ":" + mealsServicePort + "/api/v1/meals";
    }

    public MealModel getMealByMealId(String mealId) {
        String url = MEALS_SERVICE_URL + "/" + mealId;
        return restTemplate.getForObject(url, MealModel.class);
    }

    public String getMealName(String mealId) {
        String url = MEALS_SERVICE_URL + "/" + mealId + "/mealname";
        return restTemplate.getForObject(url, String.class);
    }

    public Integer getCalories(String mealId) {
        String url = MEALS_SERVICE_URL + "/" + mealId + "/calories";
        return restTemplate.getForObject(url, Integer.class);
    }
}