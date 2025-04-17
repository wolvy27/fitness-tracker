package com.fitnesstracker.apigateway.domainclientlayer.workouts;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
public class WorkoutIdentifier {
    @Column(name="workout_id")
    private String workoutId;

    public WorkoutIdentifier() {
        this.workoutId = UUID.randomUUID().toString();
    }

    public WorkoutIdentifier(String workoutId) {
        this.workoutId = workoutId;
    }
}
