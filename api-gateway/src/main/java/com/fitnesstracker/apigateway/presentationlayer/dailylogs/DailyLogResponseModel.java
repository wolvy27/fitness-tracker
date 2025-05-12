package com.fitnesstracker.apigateway.presentationlayer.dailylogs;

import com.fitnesstracker.apigateway.domainclientlayer.dailylogs.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogResponseModel extends RepresentationModel<DailyLogResponseModel> {
    private String dailyLogIdentifier;
    private String userIdentifier;
    private String userFirstName;
    private String userLastName;

    private String workoutIdentifier;
    private String workoutName;
    private Integer workoutDurationInMinutes;

    private LocalDate logDate;
    private String breakfastIdentifier;
    private String breakFastName;
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