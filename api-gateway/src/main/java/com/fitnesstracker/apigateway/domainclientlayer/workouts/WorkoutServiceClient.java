package com.fitnesstracker.apigateway.domainclientlayer.workouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutResponseModel;
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
public class WorkoutServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String WORKOUT_SERVICE_URL;

    public WorkoutServiceClient(RestTemplate restTemplate, ObjectMapper mapper,
                                @Value("${app.workouts-service.host}") String workoutServiceHost,
                                @Value("${app.workouts-service.port}") String workoutServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.WORKOUT_SERVICE_URL = "http://" + workoutServiceHost + ":" + workoutServicePort + "/api/v1/workouts";
    }

    public WorkoutResponseModel getWorkoutByWorkoutId(String workoutId) {
        try {
            String url = WORKOUT_SERVICE_URL + "/" + workoutId;
            log.debug("Getting workout by id: {}", workoutId);
            WorkoutResponseModel workoutResponseModel = restTemplate.getForObject(url, WorkoutResponseModel.class);
            return workoutResponseModel;
        }
        catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<WorkoutResponseModel> getWorkouts() {
        try {
            String url = WORKOUT_SERVICE_URL;
            log.debug("Getting workouts");
            WorkoutResponseModel[] workoutResponseModels = restTemplate.getForObject(url, WorkoutResponseModel[].class);
            return Arrays.asList(workoutResponseModels);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public WorkoutResponseModel addWorkout(WorkoutRequestModel workoutRequestModel) {
        try {
            String url = WORKOUT_SERVICE_URL;
            log.debug("Adding workout: {}", workoutRequestModel);
            WorkoutResponseModel workoutResponseModel = restTemplate.postForObject(url, workoutRequestModel, WorkoutResponseModel.class);
            return workoutResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public WorkoutResponseModel updateWorkout(WorkoutRequestModel workoutRequestModel, String workoutId) {
        try {
            String url = WORKOUT_SERVICE_URL + "/" + workoutId;
            log.debug("Updating workout: {}", workoutRequestModel);
            HttpEntity<WorkoutRequestModel> requestEntity = new HttpEntity<>(workoutRequestModel);
            WorkoutResponseModel workoutResponseModel = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                    new ParameterizedTypeReference<WorkoutResponseModel>(){}).getBody();
            return workoutResponseModel;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteWorkout(String workoutId) {
        try {
            String url = WORKOUT_SERVICE_URL + "/" + workoutId;
            log.debug("Deleting workout: {}", workoutId);
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
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
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}