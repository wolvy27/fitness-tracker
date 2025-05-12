package com.fitnesstracker.dailylogs.domainclientlayer.workouts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class WorkoutModel {
    private String workoutId;
    private String workoutName;
    private Integer workoutDurationInMinutes;
}
