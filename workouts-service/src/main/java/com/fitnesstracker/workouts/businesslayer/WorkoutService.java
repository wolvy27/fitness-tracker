package com.fitnesstracker.workouts.businesslayer;





import com.fitnesstracker.workouts.presentationlayer.WorkoutRequestModel;
import com.fitnesstracker.workouts.presentationlayer.WorkoutResponseModel;

import java.util.List;

public interface WorkoutService {
    List<WorkoutResponseModel> getWorkouts();
    WorkoutResponseModel getWorkoutByWorkoutId(String workoutId);
    WorkoutResponseModel addWorkout(WorkoutRequestModel newWorkoutData);
    WorkoutResponseModel updateWorkout(WorkoutRequestModel newWorkoutData, String workoutId);
    void deleteWorkout(String workoutId);

    // For DailyLog aggregate
    //String getWorkoutNameByWorkoutId(String workoutId, String userId);
    //Integer getDurationInMinutesByWorkoutId(String workoutId, String userId);
}
