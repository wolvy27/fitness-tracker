package com.fitnesstracker.apigateway.presentationlayer.meals;

import com.fitnesstracker.apigateway.businesslayer.meals.MealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/meals")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MealResponseModel>> getMeals() {
        log.info("Retrieving all meals");
        return ResponseEntity.ok(mealService.getMeals());
    }

    @GetMapping(value = "/{mealId}", produces = "application/json")
    public ResponseEntity<MealResponseModel> getMeal(@PathVariable String mealId) {
        log.info("Retrieving meal with ID: {}", mealId);
        return ResponseEntity.ok(mealService.getMealByMealId(mealId));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<MealResponseModel> addMeal(@RequestBody MealRequestModel mealRequestModel) {
        log.debug("Creating new meal");
        return ResponseEntity.status(HttpStatus.CREATED).body(mealService.addMeal(mealRequestModel));
    }

    @PutMapping(value = "/{mealId}", produces = "application/json")
    public ResponseEntity<MealResponseModel> updateMeal(
            @PathVariable String mealId,
            @RequestBody MealRequestModel mealRequestModel) {
        log.debug("Updating meal with ID: {}", mealId);
        return ResponseEntity.ok(mealService.updateMeal(mealRequestModel, mealId));
    }

    @DeleteMapping(value = "/{mealId}", produces = "application/json")
    public ResponseEntity<Void> deleteMeal(@PathVariable String mealId) {
        log.debug("Deleting meal with ID: {}", mealId);
        mealService.deleteMeal(mealId);
        return ResponseEntity.noContent().build();
    }
}