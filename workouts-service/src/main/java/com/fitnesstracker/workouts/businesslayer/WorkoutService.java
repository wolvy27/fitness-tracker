package com.fitnesstracker.workouts.businesslayer;





import com.fitnesstracker.workouts.presentationlayer.WorkoutRequestModel;
import com.fitnesstracker.workouts.presentationlayer.WorkoutResponseModel;

import java.util.List;

public interface WorkoutService {
    List<WorkoutResponseModel> getWorkouts(String userId);
    WorkoutResponseModel getWorkoutByWorkoutId(String workoutId, String userId);
    WorkoutResponseModel addWorkout(WorkoutRequestModel newWorkoutData, String userId);
    WorkoutResponseModel updateWorkout(WorkoutRequestModel newWorkoutData, String workoutId, String userId);
    void deleteWorkout(String workoutId, String userId);

    // For DailyLog aggregate
    String getWorkoutNameByWorkoutId(String workoutId, String userId);
    Integer getDurationInMinutesByWorkoutId(String workoutId, String userId);
}
