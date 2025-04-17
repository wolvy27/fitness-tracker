package com.fitnesstracker.apigateway.presentationlayer.workouts;

import com.fitnesstracker.apigateway.domainclientlayer.workouts.WorkoutType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRequestModel {
    //private String userId;
    private String workoutName;
    private WorkoutType workoutType;
    private Integer durationInMinutes;
    private LocalDate workoutDate;
}
