package com.fitnesstracker.dailylogs.presentationlayer;


import com.fitnesstracker.dailylogs.dataaccesslayer.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class DailyLogResponseModel {
    private String dailyLogIdentifier;
    private String userIdentifier;
    private String userFirstName;
    private String userLastName;

    private String workoutIdentifier;
    private String workoutName;
    private Integer workoutDurationInMinutes;




    private LocalDate logDate;
    private String breakfastIdentifier;
    private String breakfastName;
    private int breakfastCalories;
    private String lunchIdentifier;
    private String lunchName;
    private int lunchCalories;
    private String dinnerIdentifier;
    private String dinnerName;
    private int dinnerCalories;
    private List<String> snacksIdentifier;
    private List<String> snacksName;
    private List<Integer> snacksCalories;
    private GoalStatus metCaloriesGoal;
    private GoalStatus metWorkoutGoal;
    private GoalStatus metDailyGoals;
}
