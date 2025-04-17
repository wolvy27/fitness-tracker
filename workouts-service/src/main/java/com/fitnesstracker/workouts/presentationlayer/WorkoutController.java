package com.fitnesstracker.workouts.presentationlayer;


import com.fitnesstracker.workouts.businesslayer.WorkoutService;
import com.fitnesstracker.workouts.utils.exceptions.NotFoundException;
import com.fitnesstracker.workouts.utils.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    private static final int UUID_LENGTH = 36;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutResponseModel>> getWorkouts() {
        return ResponseEntity.ok().body(workoutService.getWorkouts());
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseModel> getWorkout(@PathVariable String workoutId) {
        if (workoutId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid workoutId provided: " + workoutId);
        }
        WorkoutResponseModel workout = workoutService.getWorkoutByWorkoutId(workoutId);
        if (workout == null && workoutId.length() == UUID_LENGTH) {
            throw new NotFoundException("Meal not found: " + workoutId);
        }
        return ResponseEntity.ok().body(workoutService.getWorkoutByWorkoutId(workoutId));
    }

    @PostMapping()
    public ResponseEntity<WorkoutResponseModel> addWorkout(@RequestBody WorkoutRequestModel workoutRequestModel) {
        return ResponseEntity.ok().body(workoutService.addWorkout(workoutRequestModel));
    }

    @PutMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseModel> updateWorkout(@RequestBody WorkoutRequestModel workoutRequestModel, @PathVariable String workoutId) {
        if (workoutId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid workoutId provided: " + workoutId);
        }
        WorkoutResponseModel workout = workoutService.getWorkoutByWorkoutId(workoutId);
        if (workout == null && workoutId.length() == UUID_LENGTH) {
            throw new NotFoundException("Meal not found: " + workoutId);
        }
        return ResponseEntity.created(null).body(workoutService.updateWorkout(workoutRequestModel, workoutId));
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String workoutId) {
        if (workoutId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid workoutId provided: " + workoutId);
        }
        WorkoutResponseModel workout = workoutService.getWorkoutByWorkoutId(workoutId);
        if (workout == null && workoutId.length() == UUID_LENGTH) {
            throw new NotFoundException("Meal not found: " + workoutId);
        }
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
