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
public class DailyLogRequestModel {
    private String workoutIdentifier;
    private List<String> mealIdentifiers;
    private LocalDate logDate;
    private String breakfast;
    private String lunch;
    private String dinner;
    private List<String> snacks;
    private GoalStatus metCaloriesGoal;
    private GoalStatus metWorkoutGoal;
    private GoalStatus metDailyGoals;
}
