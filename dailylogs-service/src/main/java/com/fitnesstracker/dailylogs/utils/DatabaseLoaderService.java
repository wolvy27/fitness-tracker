package com.fitnesstracker.dailylogs.utils;

import com.fitnesstracker.dailylogs.dataaccesslayer.*;
import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    private DailyLogRepository dailyLogRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        dailyLogRepository.deleteAll();

        // Create default empty models
        WorkoutModel emptyWorkout = WorkoutModel.builder()
                .workoutId("None")
                .workoutName("")
                .workoutDurationInMinutes(0)
                .build();

        MealModel emptyMeal = MealModel.builder()
                .mealId("None")
                .mealName("")
                .mealCalorie(0)
                .build();

        // ========== ALICE ==========
        UserModel alice = UserModel.builder()
                .userId("2dbdcb96-6479-430b-860f-bf99fd854940")
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        WorkoutModel aliceWorkout1 = WorkoutModel.builder()
                .workoutId("38f8492b-4d5a-4326-9981-32bffb132938")
                .workoutName("Cardio")
                .workoutDurationInMinutes(45)
                .build();

        MealModel aliceBreakfast = MealModel.builder()
                .mealId("a1b2c3d4-e5f6-47g8-h9i0-j1k2l3m4n5o6")
                .mealName("Overnight Oats")
                .mealCalorie(350)
                .build();

        MealModel aliceLunch = MealModel.builder()
                .mealId("b2c3d4e5-f6g7-58h9-i0j1-k2l3m4n5o6p7")
                .mealName("Chicken Salad")
                .mealCalorie(450)
                .build();

        DailyLog aliceLog1 = DailyLog.builder()
                .id("d1a1b1c1-d1e1-41f1-g1h1-i1j1k1l1m1n1")
                .userModel(alice)
                .workoutModel(aliceWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("d1a1b1c1-d1e1-41f1-g1h1-i1j1k1l1m1n1"))
                .breakfast(aliceBreakfast)
                .lunch(aliceLunch)
                .dinner(emptyMeal)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== BOB ==========
        UserModel bob = UserModel.builder()
                .userId("1a0d6533-f13b-42fb-afd6-7dfd3eb95510")
                .firstName("Bob")
                .lastName("Smith")
                .build();

        WorkoutModel bobWorkout1 = WorkoutModel.builder()
                .workoutId("fd8b9e10-1b3b-4d82-b2c6-4c9ed0fcb647")
                .workoutName("Strength")
                .workoutDurationInMinutes(50)
                .build();

        MealModel bobBreakfast = MealModel.builder()
                .mealId("c3d4e5f6-g7h8-69i0-j1k2-l3m4n5o6p7q8")
                .mealName("Protein Smoothie")
                .mealCalorie(300)
                .build();

        MealModel bobDinner = MealModel.builder()
                .mealId("d4e5f6g7-h8i9-70j1-k2l3-m4n5o6p7q8r9")
                .mealName("Steak with Vegetables")
                .mealCalorie(700)
                .build();

        DailyLog bobLog1 = DailyLog.builder()
                .id("d4a4b4c4-d4e4-44f4-g4h4-i4j4k4l4m4n4")
                .userModel(bob)
                .workoutModel(bobWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("d4a4b4c4-d4e4-44f4-g4h4-i4j4k4l4m4n4"))
                .breakfast(bobBreakfast)
                .lunch(emptyMeal)
                .dinner(bobDinner)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== CHARLIE ==========
        UserModel charlie = UserModel.builder()
                .userId("7554db22-2104-4d9b-b80a-93c96ecd784a")
                .firstName("Charlie")
                .lastName("Davis")
                .build();

        WorkoutModel charlieWorkout1 = WorkoutModel.builder()
                .workoutId("c27f56a7-b2c5-4e4d-93b3-6d47300d4fd7")
                .workoutName("HIIT")
                .workoutDurationInMinutes(30)
                .build();

        MealModel charlieBreakfast = MealModel.builder()
                .mealId("e5f6g7h8-i9j0-81k2-l3m4-n5o6p7q8r9s0")
                .mealName("Avocado Toast")
                .mealCalorie(380)
                .build();

        MealModel charlieSnack = MealModel.builder()
                .mealId("f6g7h8i9-j0k1-92l3-m4n5-o6p7q8r9s0t1")
                .mealName("Protein Bar")
                .mealCalorie(200)
                .build();

        DailyLog charlieLog1 = DailyLog.builder()
                .id("d7a7b7c7-d7e7-47f7-g7h7-i7j7k7l7m7n7")
                .userModel(charlie)
                .workoutModel(charlieWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("d7a7b7c7-d7e7-47f7-g7h7-i7j7k7l7m7n7"))
                .breakfast(charlieBreakfast)
                .lunch(emptyMeal)
                .dinner(emptyMeal)
                .snacks(List.of(charlieSnack))
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== DAVE ==========
        UserModel dave = UserModel.builder()
                .userId("941187dc-bf1f-4752-84bc-0026dc24bcd2")
                .firstName("Dave")
                .lastName("Miller")
                .build();

        WorkoutModel daveWorkout1 = WorkoutModel.builder()
                .workoutId("5b47d48a-65fd-4b1d-a0e1-cb948d3731f4")
                .workoutName("Cardio")
                .workoutDurationInMinutes(45)
                .build();

        MealModel daveLunch = MealModel.builder()
                .mealId("g7h8i9j0-k1l2-03m4-n5o6-p7q8r9s0t1u2")
                .mealName("Turkey Sandwich")
                .mealCalorie(450)
                .build();

        MealModel daveDinner = MealModel.builder()
                .mealId("h8i9j0k1-l2m3-14n5-o6p7-q8r9s0t1u2v3")
                .mealName("Pasta with Chicken")
                .mealCalorie(600)
                .build();

        DailyLog daveLog1 = DailyLog.builder()
                .id("1680bd52-1375-4c71-96c7-7a03a3d663c4")
                .userModel(dave)
                .workoutModel(daveWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("1680bd52-1375-4c71-96c7-7a03a3d663c4"))
                .breakfast(emptyMeal)
                .lunch(daveLunch)
                .dinner(daveDinner)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== EVE ==========
        UserModel eve = UserModel.builder()
                .userId("97aa92f3-7e4d-4f64-b67f-2762b6d19e0c")
                .firstName("Eve")
                .lastName("Wilson")
                .build();

        MealModel eveBreakfast = MealModel.builder()
                .mealId("i9j0k1l2-m3n4-25o6-p7q8-r9s0t1u2v3w4")
                .mealName("Greek Yogurt with Berries")
                .mealCalorie(280)
                .build();

        MealModel eveLunch = MealModel.builder()
                .mealId("j0k1l2m3-n4o5-36p7-q8r9-s0t1u2v3w4x5")
                .mealName("Quinoa Bowl with Vegetables")
                .mealCalorie(520)
                .build();

        DailyLog eveLog1 = DailyLog.builder()
                .id("c467c309-730e-479c-bd57-cd7978f54266")
                .userModel(eve)
                .workoutModel(emptyWorkout)
                .dailyLogIdentifier(new DailyLogIdentifier("c467c309-730e-479c-bd57-cd7978f54266"))
                .breakfast(eveBreakfast)
                .lunch(eveLunch)
                .dinner(emptyMeal)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.NONE)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== FRANK ==========
        UserModel frank = UserModel.builder()
                .userId("6fc89a50-3d10-48b7-819c-dc5e40fca6e9")
                .firstName("Frank")
                .lastName("Anderson")
                .build();

        WorkoutModel frankWorkout1 = WorkoutModel.builder()
                .workoutId("1b4fba67-2989-4b0e-91db-28dc8cbd8897")
                .workoutName("Strength")
                .workoutDurationInMinutes(50)
                .build();

        MealModel frankBreakfast = MealModel.builder()
                .mealId("k1l2m3n4-o5p6-47q8-r9s0-t1u2v3w4x5y6")
                .mealName("Egg White Omelette")
                .mealCalorie(320)
                .build();

        MealModel frankDinner = MealModel.builder()
                .mealId("l2m3n4o5-p6q7-58r9-s0t1-u2v3w4x5y6z7")
                .mealName("Grilled Salmon")
                .mealCalorie(550)
                .build();

        DailyLog frankLog1 = DailyLog.builder()
                .id("05a4cabe-1bc3-4268-a0b7-f73226637c77")
                .userModel(frank)
                .workoutModel(frankWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("05a4cabe-1bc3-4268-a0b7-f73226637c77"))
                .breakfast(frankBreakfast)
                .lunch(emptyMeal)
                .dinner(frankDinner)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== GRACE ==========
        UserModel grace = UserModel.builder()
                .userId("4d75e21a-8f9b-4063-bbb0-c2437a2c14b5")
                .firstName("Grace")
                .lastName("Taylor")
                .build();

        WorkoutModel graceWorkout1 = WorkoutModel.builder()
                .workoutId("e34f45bb-87e5-4667-b6b9-3df3cbf830fa")
                .workoutName("Pilates")
                .workoutDurationInMinutes(40)
                .build();

        MealModel graceBreakfast = MealModel.builder()
                .mealId("m3n4o5p6-q7r8-69s0-t1u2-v3w4x5y6z7a8")
                .mealName("Smoothie Bowl")
                .mealCalorie(420)
                .build();

        MealModel graceDinner = MealModel.builder()
                .mealId("n4o5p6q7-r8s9-70t1-u2v3-w4x5y6z7a8b9")
                .mealName("Vegetable Stir Fry")
                .mealCalorie(380)
                .build();

        DailyLog graceLog1 = DailyLog.builder()
                .id("ad205102-599f-4bda-b313-47f4c7f82a60")
                .userModel(grace)
                .workoutModel(graceWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("ad205102-599f-4bda-b313-47f4c7f82a60"))
                .breakfast(graceBreakfast)
                .lunch(emptyMeal)
                .dinner(graceDinner)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== HENRY ==========
        UserModel henry = UserModel.builder()
                .userId("82a71d95-57c6-4d39-9cbe-f6a6b4c0b73a")
                .firstName("Henry")
                .lastName("Clark")
                .build();

        WorkoutModel henryWorkout1 = WorkoutModel.builder()
                .workoutId("f75ad2be-097e-4827-b509-e59d95a83c58")
                .workoutName("Strength")
                .workoutDurationInMinutes(45)
                .build();

        MealModel henryBreakfast = MealModel.builder()
                .mealId("o5p6q7r8-s9t0-81u2-v3w4-x5y6z7a8b9c0")
                .mealName("Oatmeal with Fruit")
                .mealCalorie(340)
                .build();

        MealModel henryLunch = MealModel.builder()
                .mealId("p6q7r8s9-t0u1-92v3-w4x5-y6z7a8b9c0d1")
                .mealName("Whole Grain Pasta")
                .mealCalorie(480)
                .build();

        DailyLog henryLog1 = DailyLog.builder()
                .id("fbbdb650-a98d-476f-adad-5ed16e00d848")
                .userModel(henry)
                .workoutModel(henryWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("fbbdb650-a98d-476f-adad-5ed16e00d848"))
                .breakfast(henryBreakfast)
                .lunch(henryLunch)
                .dinner(emptyMeal)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== ISABELLA ==========
        UserModel isabella = UserModel.builder()
                .userId("3b9ef1d2-c6a8-47e5-85b0-df63b3951497")
                .firstName("Isabella")
                .lastName("Moore")
                .build();

        WorkoutModel isabellaWorkout1 = WorkoutModel.builder()
                .workoutId("f6034af9-0291-46d6-bf47-3eb65efad13c")
                .workoutName("Endurance")
                .workoutDurationInMinutes(60)
                .build();

        MealModel isabellaBreakfast = MealModel.builder()
                .mealId("q7r8s9t0-u1v2-03w4-x5y6-z7a8b9c0d1e2")
                .mealName("Peanut Butter Toast")
                .mealCalorie(350)
                .build();

        MealModel isabellaSnack = MealModel.builder()
                .mealId("r8s9t0u1-v2w3-14x5-y6z7-a8b9c0d1e2f3")
                .mealName("Energy Bar")
                .mealCalorie(180)
                .build();

        MealModel isabellaDinner = MealModel.builder()
                .mealId("s9t0u1v2-w3x4-25y6-z7a8-b9c0d1e2f3g4")
                .mealName("Carb-loading Pasta")
                .mealCalorie(680)
                .build();

        DailyLog isabellaLog1 = DailyLog.builder()
                .id("4c87fb48-4d78-4141-b14d-059bbbbe9d50")
                .userModel(isabella)
                .workoutModel(isabellaWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("4c87fb48-4d78-4141-b14d-059bbbbe9d50"))
                .breakfast(isabellaBreakfast)
                .lunch(emptyMeal)
                .dinner(isabellaDinner)
                .snacks(List.of(isabellaSnack))
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // ========== JACK ==========
        UserModel jack = UserModel.builder()
                .userId("5ae0f247-3d9b-4e58-ba3c-2f8d72ad32c1")
                .firstName("Jack")
                .lastName("Walker")
                .build();

        WorkoutModel jackWorkout1 = WorkoutModel.builder()
                .workoutId("d964b991-d7e6-4371-9734-9e63307b65b5")
                .workoutName("Martial Arts")
                .workoutDurationInMinutes(50)
                .build();

        MealModel jackBreakfast = MealModel.builder()
                .mealId("t0u1v2w3-x4y5-36z7-a8b9-c0d1e2f3g4h5")
                .mealName("Breakfast Burrito")
                .mealCalorie(550)
                .build();

        MealModel jackLunch = MealModel.builder()
                .mealId("u1v2w3x4-y5z6-47a8-b9c0-d1e2f3g4h5i6")
                .mealName("Turkey Wrap")
                .mealCalorie(420)
                .build();

        MealModel jackDinner = MealModel.builder()
                .mealId("v2w3x4y5-z6a7-58b9-c0d1-e2f3g4h5i6j7")
                .mealName("Grilled Chicken with Rice")
                .mealCalorie(580)
                .build();

        DailyLog jackLog1 = DailyLog.builder()
                .id("fdff5525-3d77-4c8b-ad73-57988d621b18")
                .userModel(jack)
                .workoutModel(jackWorkout1)
                .dailyLogIdentifier(new DailyLogIdentifier("fdff5525-3d77-4c8b-ad73-57988d621b18"))
                .breakfast(jackBreakfast)
                .lunch(jackLunch)
                .dinner(jackDinner)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // Save all logs
        dailyLogRepository.saveAll(Arrays.asList(
                aliceLog1, bobLog1, charlieLog1, daveLog1, eveLog1,
                frankLog1, graceLog1, henryLog1, isabellaLog1, jackLog1
        ));
    }
}