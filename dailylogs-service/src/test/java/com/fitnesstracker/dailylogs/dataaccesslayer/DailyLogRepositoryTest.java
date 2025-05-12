package com.fitnesstracker.dailylogs.dataaccesslayer;

import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class DailyLogRepositoryTest {

    @Autowired
    DailyLogRepository dailyLogRepository;

    private DailyLog dailyLog1;
    private DailyLog dailyLog2;
    private final String userId1 = "2dbdcb96-6479-430b-860f-bf99fd85494d";
    private final String userId2 = "7554db22-2104-4d9b-b88a-93c96ecd784a";

    @BeforeEach
    public void setupDB() {
        dailyLogRepository.deleteAll();

        // Create empty models
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

        // User 1 data
        UserModel user1 = UserModel.builder()
                .userId(userId1)
                .firstName("Alice")
                .lastName("Johnson")
                .build();

        WorkoutModel workout1 = WorkoutModel.builder()
                .workoutId("38f8492b-4d5a-4326-9981-32bffb132938")
                .workoutName("Cardio")
                .workoutDurationInMinutes(45)
                .build();

        MealModel breakfast1 = MealModel.builder()
                .mealId("alb2c3d4-e5f6-47g8-h91o-j1k213m4n5o6")
                .mealName("Overnight Oats")
                .mealCalorie(350)
                .build();

        dailyLog1 = DailyLog.builder()
                .id("dialbici-diel-4lf1-gih1-iljikllimin1")
                .userModel(user1)
                .workoutModel(workout1)
                .dailyLogIdentifier(new DailyLogIdentifier("dialbici-diel-4lf1-gih1-iljikllimin1"))
                .breakfast(breakfast1)
                .lunch(emptyMeal)
                .dinner(emptyMeal)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 10))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        // User 2 data
        UserModel user2 = UserModel.builder()
                .userId(userId2)
                .firstName("Charlie")
                .lastName("Davis")
                .build();

        WorkoutModel workout2 = WorkoutModel.builder()
                .workoutId("c27f56a7-b2c5-4e4d-93b3-6d47300d4fd7")
                .workoutName("HIIT")
                .workoutDurationInMinutes(30)
                .build();

        MealModel snack2 = MealModel.builder()
                .mealId("f6g7h819-j0k1-9213-m4n5-o6p7q8r9s0t1")
                .mealName("Protein Bar")
                .mealCalorie(200)
                .build();

        dailyLog2 = DailyLog.builder()
                .id("d7a7b7c7-d7e7-47f7-g7h7-i7j7k717m7n7")
                .userModel(user2)
                .workoutModel(workout2)
                .dailyLogIdentifier(new DailyLogIdentifier("d7a7b7c7-d7e7-47f7-g7h7-i7j7k717m7n7"))
                .breakfast(emptyMeal)
                .lunch(emptyMeal)
                .dinner(emptyMeal)
                .snacks(List.of(snack2))
                .logDate(LocalDate.of(2025, 3, 11))
                .metCaloriesGoal(GoalStatus.ACHIEVED)
                .metWorkoutGoal(GoalStatus.ACHIEVED)
                .metDailyGoals(GoalStatus.ACHIEVED)
                .build();

        dailyLogRepository.saveAll(List.of(dailyLog1, dailyLog2));
    }

    @Test
    public void whenFindAllByUserModel_userId_thenReturnDailyLogsForUser() {
        // act
        List<DailyLog> foundLogs = dailyLogRepository.findAllByUserModel_userId(userId1);

        // assert
        assertNotNull(foundLogs);
        assertEquals(1, foundLogs.size());
        assertEquals(dailyLog1.getId(), foundLogs.get(0).getId());
        assertEquals(userId1, foundLogs.get(0).getUserModel().getUserId());
    }

    @Test
    public void whenFindAllByUserModel_userIdWithInvalidUserId_thenReturnEmptyList() {
        // arrange
        String invalidUserId = "invalid-user-id";

        // act
        List<DailyLog> foundLogs = dailyLogRepository.findAllByUserModel_userId(invalidUserId);

        // assert
        assertNotNull(foundLogs);
        assertTrue(foundLogs.isEmpty());
    }

    @Test
    public void whenFindDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId_thenReturnDailyLog() {
        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(
                dailyLog1.getDailyLogIdentifier().getDailyLogId(),
                userId1
        );

        // assert
        assertNotNull(foundLog);
        assertEquals(dailyLog1.getId(), foundLog.getId());
        assertEquals(userId1, foundLog.getUserModel().getUserId());
    }

    @Test
    public void whenFindDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userIdWithInvalidId_thenReturnNull() {
        // arrange
        String invalidDailyLogId = "invalid-daily-log-id";

        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(
                invalidDailyLogId,
                userId1
        );

        // assert
        assertNull(foundLog);
    }

    @Test
    public void whenFindDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userIdWithMismatchedUser_thenReturnNull() {
        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByDailyLogIdentifier_DailyLogIdAndUserModel_userId(
                dailyLog1.getDailyLogIdentifier().getDailyLogId(),
                userId2
        );

        // assert
        assertNull(foundLog);
    }

    @Test
    public void whenFindDailyLogByLogDateAndUserModel_userId_thenReturnDailyLog() {
        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByLogDateAndUserModel_userId(
                dailyLog1.getLogDate(),
                userId1
        );

        // assert
        assertNotNull(foundLog);
        assertEquals(dailyLog1.getId(), foundLog.getId());
        assertEquals(dailyLog1.getLogDate(), foundLog.getLogDate());
        assertEquals(userId1, foundLog.getUserModel().getUserId());
    }

    @Test
    public void whenFindDailyLogByLogDateAndUserModel_userIdWithInvalidDate_thenReturnNull() {
        // arrange
        LocalDate invalidDate = LocalDate.of(2025, 1, 1);

        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByLogDateAndUserModel_userId(
                invalidDate,
                userId1
        );

        // assert
        assertNull(foundLog);
    }

    @Test
    public void whenFindDailyLogByLogDateAndUserModel_userIdWithMismatchedUser_thenReturnNull() {
        // act
        DailyLog foundLog = dailyLogRepository.findDailyLogByLogDateAndUserModel_userId(
                dailyLog1.getLogDate(),
                userId2
        );

        // assert
        assertNull(foundLog);
    }

    @Test
    public void whenSaveNewDailyLog_thenVerifySavedCorrectly() {
        // arrange
        UserModel newUser = UserModel.builder()
                .userId("new-user-id")
                .firstName("New")
                .lastName("User")
                .build();

        WorkoutModel newWorkout = WorkoutModel.builder()
                .workoutId("new-workout-id")
                .workoutName("Yoga")
                .workoutDurationInMinutes(60)
                .build();

        DailyLog newDailyLog = DailyLog.builder()
                .id("new-daily-log-id")
                .userModel(newUser)
                .workoutModel(newWorkout)
                .dailyLogIdentifier(new DailyLogIdentifier("new-daily-log-id"))
                .breakfast(null)
                .lunch(null)
                .dinner(null)
                .snacks(Collections.emptyList())
                .logDate(LocalDate.of(2025, 3, 15))
                .metCaloriesGoal(GoalStatus.IN_PROGRESS)
                .metWorkoutGoal(GoalStatus.IN_PROGRESS)
                .metDailyGoals(GoalStatus.IN_PROGRESS)
                .build();

        // act
        DailyLog savedLog = dailyLogRepository.save(newDailyLog);
        DailyLog retrievedLog = dailyLogRepository.findById(savedLog.getId()).orElse(null);

        // assert
        assertNotNull(retrievedLog);
        assertEquals(newDailyLog.getId(), retrievedLog.getId());
        assertEquals(newUser.getUserId(), retrievedLog.getUserModel().getUserId());
        assertEquals(newWorkout.getWorkoutId(), retrievedLog.getWorkoutModel().getWorkoutId());
        assertEquals(newDailyLog.getLogDate(), retrievedLog.getLogDate());
    }

    @Test
    public void whenDeleteDailyLog_thenVerifyDeleted() {
        // act
        dailyLogRepository.delete(dailyLog1);
        DailyLog deletedLog = dailyLogRepository.findById(dailyLog1.getId()).orElse(null);

        // assert
        assertNull(deletedLog);
    }

    @Test
    public void whenUpdateDailyLog_thenVerifyChangesPersisted() {
        // arrange
        String newWorkoutName = "Updated Workout";
        dailyLog1.getWorkoutModel().setWorkoutName(newWorkoutName);

        // act
        DailyLog updatedLog = dailyLogRepository.save(dailyLog1);
        DailyLog retrievedLog = dailyLogRepository.findById(updatedLog.getId()).orElse(null);

        // assert
        assertNotNull(retrievedLog);
        assertEquals(newWorkoutName, retrievedLog.getWorkoutModel().getWorkoutName());
    }
}