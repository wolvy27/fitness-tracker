package com.fitnesstracker.meals.presentationlayer;

import com.fitnesstracker.meals.businesslayer.MealService;
import com.fitnesstracker.meals.utils.exceptions.InvalidInputException;
import com.fitnesstracker.meals.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/meals")
public class MealController {
    private final MealService mealService;
    private static final int UUID_LENGTH = 36;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping()
    public ResponseEntity<List<MealResponseModel>> getMeals() {
        return ResponseEntity.ok().body(mealService.getMeals());
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealResponseModel> getMeal(@PathVariable String mealId) {
        if ("None".equals(mealId)) {
            return ResponseEntity.ok(new MealResponseModel(
                    "None",                   // mealId
                    "",                       // mealName
                    0,                       // calories
                    null,                     // mealDate
                    null                      // mealType (can be null for empty meal)
            ));
        }

        if (mealId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid mealId provided: " + mealId);
        }

        MealResponseModel meal = mealService.getMealByMealId(mealId);
        if (meal == null) {
            throw new NotFoundException("Meal not found: " + mealId);
        }
        return ResponseEntity.ok(meal);
    }

    @PostMapping()
    public ResponseEntity<MealResponseModel> addMeal(@RequestBody MealRequestModel mealRequestModel) {
        return ResponseEntity.created(null).body(mealService.addMeal(mealRequestModel));
    }

    @PutMapping("/{mealId}")
    public ResponseEntity<MealResponseModel> updateMeal(@RequestBody MealRequestModel mealRequestModel, @PathVariable String mealId) {
        MealResponseModel meal = mealService.getMealByMealId(mealId);
        if (meal == null && mealId.length() == UUID_LENGTH) {
            throw new NotFoundException("Meal not found: " + mealId);
        }
        if (mealId.length() != UUID_LENGTH) {
            throw new NotFoundException("Invalid mealId provided: " + mealId);
        }
        return ResponseEntity.created(null).body(mealService.updateMeal(mealRequestModel, mealId));
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable String mealId) {
        if (mealId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid mealId provided: " + mealId);
        }
        MealResponseModel meal = mealService.getMealByMealId(mealId);
        if (meal == null && mealId.length() == UUID_LENGTH) {
            throw new NotFoundException("Meal not found: " + mealId);
        }
        mealService.deleteMeal(mealId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{mealId}/mealname")
    public ResponseEntity<String> getMealName(@PathVariable String mealId) {
        if (mealId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid mealId provided: " + mealId);
        }
        return ResponseEntity.ok().body(mealService.getMealNameByMealId(mealId));
    }

    @GetMapping("/{mealId}/calories")
    public ResponseEntity<Integer> getCalories(@PathVariable String mealId) {
        if (mealId.length() != UUID_LENGTH) {
            throw new InvalidInputException("Invalid mealId provided: " + mealId);
        }
        return ResponseEntity.ok().body(mealService.getCaloriesByMealId(mealId));
    }

}
