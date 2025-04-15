package com.fitnesstracker.workouts.dataaccesslayer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="workouts")
@Data
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private WorkoutIdentifier workoutIdentifier;

    @Embedded
    private UserIdentifier userIdentifier;

    @Column(name="workout_name")
    private String workoutName;

    @Enumerated(EnumType.STRING)
    @Column(name="workout_type")
    private WorkoutType workoutType;

    @Column(name="duration_in_minutes")
    private Integer durationInMinutes;

    @Column(name="workout_date")
    private LocalDate workoutDate;
}
