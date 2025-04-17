package com.fitnesstracker.meals.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class MealRepositoryTest {
    @Autowired
    MealRepository mealRepository;

    @BeforeEach
    public void setUpDb() {
        mealRepository.deleteAll();
    }

    @Test
    public void whenMealIsValid_thenAddMeal() {
        // Arrange
        Meal meal = new Meal();
        meal.setMealIdentifier(new MealIdentifier());
        meal.setMealName("Chicken Salad");
        meal.setCalories(450);
        meal.setMealDate(LocalDate.now());
        meal.setMealType(MealType.LUNCH);

        // Act
        Meal savedMeal = mealRepository.save(meal);

        // Assert
        assertNotNull(savedMeal);
        assertNotNull(savedMeal.getId());
        assertNotNull(savedMeal.getMealIdentifier());
        assertNotNull(savedMeal.getMealIdentifier().getMealId());
        assertEquals(meal.getMealName(), savedMeal.getMealName());
        assertEquals(meal.getCalories(), savedMeal.getCalories());
        assertEquals(meal.getMealDate(), savedMeal.getMealDate());
        assertEquals(meal.getMealType(), savedMeal.getMealType());
    }

    @Test
    public void whenMealExists_thenReturnMealByMealId() {
        // Arrange
        Meal meal1 = new Meal();
        meal1.setMealIdentifier(new MealIdentifier("test-meal-id-1"));
        meal1.setMealName("Oatmeal");
        meal1.setCalories(300);
        meal1.setMealDate(LocalDate.now().minusDays(1));
        meal1.setMealType(MealType.BREAKFAST);
        mealRepository.save(meal1);

        Meal meal2 = new Meal();
        meal2.setMealIdentifier(new MealIdentifier());
        meal2.setMealName("Pasta");
        meal2.setCalories(600);
        meal2.setMealDate(LocalDate.now());
        meal2.setMealType(MealType.DINNER);
        mealRepository.save(meal2);

        // Act
        Meal foundMeal = mealRepository.findMealByMealIdentifier_MealId("test-meal-id-1");

        // Assert
        assertNotNull(foundMeal);
        assertEquals(meal1.getMealIdentifier().getMealId(), foundMeal.getMealIdentifier().getMealId());
        assertEquals(meal1.getMealName(), foundMeal.getMealName());
        assertEquals(meal1.getCalories(), foundMeal.getCalories());
        assertEquals(meal1.getMealDate(), foundMeal.getMealDate());
        assertEquals(meal1.getMealType(), foundMeal.getMealType());
    }

    @Test
    public void whenMealDoesNotExist_thenReturnNull() {
        // Arrange
        final String NOT_FOUND_MEAL_ID = UUID.randomUUID().toString();

        // Act
        Meal foundMeal = mealRepository.findMealByMealIdentifier_MealId(NOT_FOUND_MEAL_ID);

        // Assert
        assertNull(foundMeal);
    }

    // Positive Test: Saving a meal with zero calories
    @Test
    public void whenSaveMealWithZeroCalories_thenMealIsSaved() {
        // Arrange
        Meal meal = new Meal();
        meal.setMealIdentifier(new MealIdentifier());
        meal.setMealName("Water");
        meal.setCalories(0);
        meal.setMealDate(LocalDate.now());
        meal.setMealType(MealType.NONE);

        // Act
        Meal savedMeal = mealRepository.save(meal);

        // Assert
        assertNotNull(savedMeal);
        assertEquals(0, savedMeal.getCalories());
    }

    // Positive Test: Saving a meal with a positive number of calories
    @Test
    public void whenSaveMealWithPositiveCalories_thenMealIsSaved() {
        // Arrange
        Meal meal = new Meal();
        meal.setMealIdentifier(new MealIdentifier());
        meal.setMealName("Fruit Salad");
        meal.setCalories(150);
        meal.setMealDate(LocalDate.now());
        meal.setMealType(MealType.SNACK);

        // Act
        Meal savedMeal = mealRepository.save(meal);

        // Assert
        assertNotNull(savedMeal);
        assertEquals(150, savedMeal.getCalories());
    }


}