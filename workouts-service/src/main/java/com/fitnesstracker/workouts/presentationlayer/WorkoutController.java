package com.fitnesstracker.workouts.presentationlayer;


import com.fitnesstracker.workouts.businesslayer.WorkoutService;
import com.fitnesstracker.workouts.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/{userId}/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    private static final int UUID_LENGTH = 36;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping()
    public ResponseEntity<List<WorkoutResponseModel>> getWorkouts(@PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(workoutService.getWorkouts(userId));
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseModel> getWorkout(@PathVariable String workoutId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }
        if (workoutId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid workoutId provided: " + workoutId);
        }
        return ResponseEntity.ok().body(workoutService.getWorkoutByWorkoutId(workoutId, userId));
    }

    @PostMapping()
    public ResponseEntity<WorkoutResponseModel> addWorkout(@PathVariable String userId, @RequestBody WorkoutRequestModel workoutRequestModel) {
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }
        return ResponseEntity.ok().body(workoutService.addWorkout(workoutRequestModel, userId));
    }

    @PutMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseModel> updateWorkout(@RequestBody WorkoutRequestModel workoutRequestModel, @PathVariable String workoutId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }
        if (workoutId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid workoutId provided: " + workoutId);
        }
        return ResponseEntity.created(null).body(workoutService.updateWorkout(workoutRequestModel, workoutId, userId));
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String workoutId, @PathVariable String userId) {
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }
        if (workoutId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid workoutId provided: " + workoutId);
        }
        workoutService.deleteWorkout(workoutId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
