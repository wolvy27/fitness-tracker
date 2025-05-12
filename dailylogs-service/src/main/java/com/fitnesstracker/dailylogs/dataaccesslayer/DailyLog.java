package com.fitnesstracker.dailylogs.dataaccesslayer;


import com.fitnesstracker.dailylogs.domainclientlayer.meals.MealModel;
import com.fitnesstracker.dailylogs.domainclientlayer.users.UserModel;
import com.fitnesstracker.dailylogs.domainclientlayer.workouts.WorkoutModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "dailylogs")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class DailyLog {

    @Id
    private String id;

    private UserModel userModel;

    private WorkoutModel workoutModel;

    private DailyLogIdentifier dailyLogIdentifier;



    private MealModel breakfast;
    private MealModel lunch;
    private MealModel dinner;
    private List<MealModel> snacks;

    private LocalDate logDate;

    private GoalStatus metCaloriesGoal;

    private GoalStatus metWorkoutGoal;

    private GoalStatus metDailyGoals;
}
