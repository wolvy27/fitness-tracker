package com.fitnesstracker.workouts.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WorkoutRepositoryTest {
    @Autowired
    WorkoutRepository workoutRepository;

    @BeforeEach
    public void setUpDb() {
        workoutRepository.deleteAll();
    }

    @Test
    public void whenWorkoutIsValid_thenAddWorkout() {
        // Arrange
        Workout workout = new Workout();
        workout.setWorkoutIdentifier(new WorkoutIdentifier());
        workout.setWorkoutName("Morning Run");
        workout.setDurationInMinutes(30);
        workout.setWorkoutDate(LocalDate.now());
        workout.setWorkoutType(WorkoutType.CARDIO);

        // Act
        Workout savedWorkout = workoutRepository.save(workout);

        // Assert
        assertNotNull(savedWorkout);
        assertNotNull(savedWorkout.getId());
        assertNotNull(savedWorkout.getWorkoutIdentifier());
        assertNotNull(savedWorkout.getWorkoutIdentifier().getWorkoutId());
        assertEquals(workout.getWorkoutName(), savedWorkout.getWorkoutName());
        assertEquals(workout.getDurationInMinutes(), savedWorkout.getDurationInMinutes());
        assertEquals(workout.getWorkoutDate(), savedWorkout.getWorkoutDate());
        assertEquals(workout.getWorkoutType(), savedWorkout.getWorkoutType());
    }

    @Test
    public void whenWorkoutExists_thenReturnWorkoutByWorkoutId() {
        // Arrange
        Workout workout1 = new Workout();
        workout1.setWorkoutIdentifier(new WorkoutIdentifier("test-workout-id-1"));
        workout1.setWorkoutName("Yoga Session");
        workout1.setDurationInMinutes(45);
        workout1.setWorkoutDate(LocalDate.now().minusDays(1));
        workout1.setWorkoutType(WorkoutType.FLEXIBILITY);
        workoutRepository.save(workout1);

        Workout workout2 = new Workout();
        workout2.setWorkoutIdentifier(new WorkoutIdentifier());
        workout2.setWorkoutName("Weight Training");
        workout2.setDurationInMinutes(60);
        workout2.setWorkoutDate(LocalDate.now());
        workout2.setWorkoutType(WorkoutType.STRENGTH);
        workoutRepository.save(workout2);

        // Act
        Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutId("test-workout-id-1");

        // Assert
        assertNotNull(foundWorkout);
        assertEquals(workout1.getWorkoutIdentifier().getWorkoutId(), foundWorkout.getWorkoutIdentifier().getWorkoutId());
        assertEquals(workout1.getWorkoutName(), foundWorkout.getWorkoutName());
        assertEquals(workout1.getDurationInMinutes(), foundWorkout.getDurationInMinutes());
        assertEquals(workout1.getWorkoutDate(), foundWorkout.getWorkoutDate());
        assertEquals(workout1.getWorkoutType(), foundWorkout.getWorkoutType());
    }

    @Test
    public void whenWorkoutDoesNotExist_thenReturnNull() {
        // Arrange
        final String NOT_FOUND_WORKOUT_ID = UUID.randomUUID().toString();

        // Act
        Workout foundWorkout = workoutRepository.findWorkoutByWorkoutIdentifier_WorkoutId(NOT_FOUND_WORKOUT_ID);

        // Assert
        assertNull(foundWorkout);
    }

    // Positive Test: Saving a workout with zero duration
    @Test
    public void whenSaveWorkoutWithZeroDuration_thenWorkoutIsSaved() {
        // Arrange
        Workout workout = new Workout();
        workout.setWorkoutIdentifier(new WorkoutIdentifier());
        workout.setWorkoutName("Stretching");
        workout.setDurationInMinutes(0);
        workout.setWorkoutDate(LocalDate.now());
        workout.setWorkoutType(WorkoutType.FLEXIBILITY);

        // Act
        Workout savedWorkout = workoutRepository.save(workout);

        // Assert
        assertNotNull(savedWorkout);
        assertEquals(0, savedWorkout.getDurationInMinutes());
    }

    // Positive Test: Saving a workout with a positive duration
    @Test
    public void whenSaveWorkoutWithPositiveDuration_thenWorkoutIsSaved() {
        // Arrange
        Workout workout = new Workout();
        workout.setWorkoutIdentifier(new WorkoutIdentifier());
        workout.setWorkoutName("HIIT Session");
        workout.setDurationInMinutes(20);
        workout.setWorkoutDate(LocalDate.now());
        workout.setWorkoutType(WorkoutType.HIGH_INTENSITY);

        // Act
        Workout savedWorkout = workoutRepository.save(workout);

        // Assert
        assertNotNull(savedWorkout);
        assertEquals(20, savedWorkout.getDurationInMinutes());
    }
}