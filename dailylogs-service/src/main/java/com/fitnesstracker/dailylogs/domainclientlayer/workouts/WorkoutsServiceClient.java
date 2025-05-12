package com.fitnesstracker.dailylogs.domainclientlayer.workouts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkoutsServiceClient {

    private final RestTemplate restTemplate;
    private final String WORKOUTS_SERVICE_URL;

    public WorkoutsServiceClient(RestTemplate restTemplate,
                                 @Value("${app.workouts-service.host}") String workoutsServiceHost,
                                 @Value("${app.workouts-service.port}") String workoutsServicePort) {
        this.restTemplate = restTemplate;
        this.WORKOUTS_SERVICE_URL = "http://" + workoutsServiceHost + ":" + workoutsServicePort + "/api/v1/workouts";
    }

    public WorkoutModel getWorkoutByWorkoutId(String workoutId) {
        String url = WORKOUTS_SERVICE_URL + "/" + workoutId;
        return restTemplate.getForObject(url, WorkoutModel.class);
    }

    public String getWorkoutName(String workoutId) {
        String url = WORKOUTS_SERVICE_URL + "/" + workoutId + "/workoutname";
        return restTemplate.getForObject(url, String.class);
    }

    public Integer getWorkoutDuration(String workoutId) {
        String url = WORKOUTS_SERVICE_URL + "/" + workoutId + "/duration";
        return restTemplate.getForObject(url, Integer.class);
    }
}