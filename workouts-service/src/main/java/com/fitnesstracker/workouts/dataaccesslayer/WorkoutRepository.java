package com.fitnesstracker.workouts.dataaccesslayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    //Workout findWorkoutByWorkoutIdentifier_WorkoutIdAndUserIdentifier_UserId(String workoutId, String userId);
    Workout findWorkoutByWorkoutIdentifier_WorkoutId(String workoutId);
    //List<Workout> findAllByUserIdentifier_UserId(String userId);
}
