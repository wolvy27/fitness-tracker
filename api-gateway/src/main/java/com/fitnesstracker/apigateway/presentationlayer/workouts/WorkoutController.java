package com.fitnesstracker.apigateway.presentationlayer.workouts;

import com.fitnesstracker.apigateway.businesslayer.workouts.WorkoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping(value = "/{workoutId}", produces = "application/json")
    public ResponseEntity<WorkoutResponseModel> getWorkout(@PathVariable String workoutId) {
        log.info("getWorkout: workoutId={}", workoutId);
        return ResponseEntity.ok(workoutService.getWorkoutByWorkoutId(workoutId));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<WorkoutResponseModel>> getWorkouts() {
        log.info("getWorkouts called");
        return ResponseEntity.ok(workoutService.getWorkouts());
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<WorkoutResponseModel> addWorkout(@RequestBody WorkoutRequestModel workoutRequestModel) {
        log.debug("addWorkout called");
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.addWorkout(workoutRequestModel));
    }

    @PutMapping(value = "/{workoutId}", produces = "application/json")
    public ResponseEntity<WorkoutResponseModel> updateWorkout(@PathVariable String workoutId,
                                                              @RequestBody WorkoutRequestModel workoutRequestModel) {
        log.debug("updateWorkout called");
        return ResponseEntity.ok(workoutService.updateWorkout(workoutRequestModel, workoutId));
    }

    @DeleteMapping(value = "/{workoutId}", produces = "application/json")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String workoutId) {
        log.debug("deleteWorkout called");
        workoutService.deleteWorkout(workoutId);
        return ResponseEntity.noContent().build();
    }
}