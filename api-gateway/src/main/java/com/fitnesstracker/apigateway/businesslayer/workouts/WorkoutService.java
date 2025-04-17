package com.fitnesstracker.apigateway.businesslayer.workouts;

import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutRequestModel;
import com.fitnesstracker.apigateway.presentationlayer.workouts.WorkoutResponseModel;

import java.util.List;

public interface WorkoutService {
    List<WorkoutResponseModel> getWorkouts();
    WorkoutResponseModel getWorkoutByWorkoutId(String workoutId);
    WorkoutResponseModel addWorkout(WorkoutRequestModel workoutRequestModel);
    WorkoutResponseModel updateWorkout(WorkoutRequestModel workoutRequestModel, String workoutId);
    void deleteWorkout(String workoutId);
}