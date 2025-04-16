package com.fitnesstracker.meals.dataaccesslayer;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {
    //Meal findMealByMealIdentifier_MealIdAndUserIdentifier_UserId(String mealId, String userId);
    Meal findMealByMealIdentifier_MealId(String mealId);
    //List<Meal> findAllByUserIdentifier_UserId(String userId);
}
