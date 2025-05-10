package presentationlayer;


import dataaccesslayer.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogResponseModel {
    private String dailyLogIdentifier;
    private String userIdentifier;
    private String userFirstName;
    private String userLastName;

    private String workoutIdentifier;
    private String workoutName;
    private Integer workoutDurationInMinutes;

    private List<String> mealIdentifiers;
    private List<String> mealNames;
    private List<Integer> mealCalories;


    private LocalDate logDate;
    private String breakfast;
    private String lunch;
    private String dinner;
    private List<String> snacks;
    private GoalStatus metCaloriesGoal;
    private GoalStatus metWorkoutGoal;
    private GoalStatus metDailyGoals;
}
