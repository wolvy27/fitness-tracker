package com.fitnesstracker.apigateway.businesslayer.workouts;

import com.fitnesstracker.apigateway.domainclientlayer.workouts.WorkoutServiceClient;
import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutServiceClient workoutServiceClient;

    public WorkoutServiceImpl(WorkoutServiceClient workoutServiceClient) {
        this.workoutServiceClient = workoutServiceClient;
    }

    @Override
    public List<WorkoutResponseModel> getWorkouts() {
        return workoutServiceClient.getWorkouts();
    }

    @Override
    public WorkoutResponseModel getWorkoutByWorkoutId(String workoutId) {
        return workoutServiceClient.getWorkoutByWorkoutId(workoutId);
    }

    @Override
    public WorkoutResponseModel addWorkout(WorkoutRequestModel workoutRequestModel) {
        return workoutServiceClient.addWorkout(workoutRequestModel);
    }

    @Override
    public WorkoutResponseModel updateWorkout(WorkoutRequestModel workoutRequestModel, String workoutId) {
        return workoutServiceClient.updateWorkout(workoutRequestModel, workoutId);
    }

    @Override
    public void deleteWorkout(String workoutId) {
        workoutServiceClient.deleteWorkout(workoutId);
    }
}