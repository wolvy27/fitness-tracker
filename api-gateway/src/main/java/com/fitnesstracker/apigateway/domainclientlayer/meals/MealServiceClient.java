package com.fitnesstracker.apigateway.domainclientlayer.meals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.presentationlayer.meals.MealRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.meals.MealResponseModel;
import com.fitnesstracker.apigateway.utils.exceptions.InvalidInputException;
import com.fitnesstracker.apigateway.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fitnesstracker.apigateway.utils.HttpErrorInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class MealServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String MEAL_SERVICE_URL;

    public MealServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                             @Value("${app.meals-service.host}") String mealServiceHost,
                             @Value("${app.meals-service.port}") String mealServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.MEAL_SERVICE_URL = "http://" + mealServiceHost + ":" + mealServicePort + "/api/v1/meals";
    }

    public MealResponseModel getMealByMealId(String mealId) {
        try {
            String url = MEAL_SERVICE_URL + "/" + mealId;
            log.debug("Getting meal by id: {}", mealId);
            return restTemplate.getForObject(url, MealResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<MealResponseModel> getMeals() {
        try {
            String url = MEAL_SERVICE_URL;
            log.debug("Getting all meals");
            MealResponseModel[] meals = restTemplate.getForObject(url, MealResponseModel[].class);
            return Arrays.asList(meals);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public MealResponseModel addMeal(MealRequestModel mealRequestModel) {
        try {
            String url = MEAL_SERVICE_URL;
            log.debug("Adding meal: {}", mealRequestModel);
            return restTemplate.postForObject(url, mealRequestModel, MealResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public MealResponseModel updateMeal(MealRequestModel mealRequestModel, String mealId) {
        try {
            String url = MEAL_SERVICE_URL + "/" + mealId;
            log.debug("Updating meal: {}", mealId);
            HttpEntity<MealRequestModel> requestEntity = new HttpEntity<>(mealRequestModel);
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                    new ParameterizedTypeReference<MealResponseModel>(){}).getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteMeal(String mealId) {
        try {
            String url = MEAL_SERVICE_URL + "/" + mealId;
            log.debug("Deleting meal: {}", mealId);
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
        }
        log.warn("Unexpected HTTP error: {}", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}