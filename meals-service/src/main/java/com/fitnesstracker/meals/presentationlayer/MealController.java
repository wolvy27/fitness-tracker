package com.fitnesstracker.meals.presentationlayer;

import com.fitnesstracker.meals.businesslayer.MealService;
import com.fitnesstracker.meals.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/{userId}/meals")
public class MealController {
    private final MealService mealService;
    private static final int UUID_LENGTH = 36;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping()
    public ResponseEntity<List<MealResponseModel>> getMeals() {
        /*
        if (userId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid userId provided: " + userId);
        }

         */
        return ResponseEntity.ok().body(mealService.getMeals());
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealResponseModel> getMeal(@PathVariable String mealId) {
        /*
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

         */
        if (mealId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid mealId provided: " + mealId);
        }
        return ResponseEntity.ok().body(mealService.getMealByMealId(mealId));
    }

    @PostMapping()
    public ResponseEntity<MealResponseModel> addMeal(@RequestBody MealRequestModel mealRequestModel) {
        /*
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

         */
        return ResponseEntity.created(null).body(mealService.addMeal(mealRequestModel));
    }

    @PutMapping("/{mealId}")
    public ResponseEntity<MealResponseModel> updateMeal(@RequestBody MealRequestModel mealRequestModel, @PathVariable String mealId) {
        /*
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

         */
        if (mealId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid mealId provided: " + mealId);
        }
        return ResponseEntity.created(null).body(mealService.updateMeal(mealRequestModel, mealId));
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable String mealId) {
        /*
        if (userId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid userId provided: " + userId);
        }

         */
        if (mealId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid mealId provided: " + mealId);
        }
        mealService.deleteMeal(mealId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
