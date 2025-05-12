package com.fitnesstracker.dailylogs.domainclientlayer.users;

import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class UsersServiceClient {

    private final RestTemplate restTemplate;
    private final String USERS_SERVICE_URL;

    public UsersServiceClient(RestTemplate restTemplate,
                              @Value("${app.users-service.host}") String usersServiceHost,
                              @Value("${app.users-service.port}") String usersServicePort) {
        this.restTemplate = restTemplate;
        this.USERS_SERVICE_URL = "http://" + usersServiceHost + ":" + usersServicePort + "/api/v1/users";
    }

    public UserModel getUserByUserId(String userId) {
        String url = USERS_SERVICE_URL + "/" + userId;
        try {
            return restTemplate.getForObject(url, UserModel.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("User with ID " + userId + " not found.");
        }
    }

    public String getUserFirstName(String userId) {
        String url = USERS_SERVICE_URL + "/" + userId + "/firstname";
        return restTemplate.getForObject(url, String.class);
    }

    public String getUserLastName(String userId) {
        String url = USERS_SERVICE_URL + "/" + userId + "/lastname";
        return restTemplate.getForObject(url, String.class);
    }

    public Integer getDailyCalorieIntake(String userId) {
        String url = USERS_SERVICE_URL + "/" + userId + "/dailycalorieintake";
        return restTemplate.getForObject(url, Integer.class);
    }

    public List<String> getWorkoutDays(String userId) {
        String url = USERS_SERVICE_URL + "/" + userId + "/workoutdays";
        return restTemplate.getForObject(url, List.class);
    }
}