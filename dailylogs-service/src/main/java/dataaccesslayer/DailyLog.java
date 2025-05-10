package dataaccesslayer;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "sales")
@Data
@Builder
@AllArgsConstructor
public class DailyLog {

    @Id
    private String id;

    private UserModel userModel;

    private WorkoutModel workoutModel;

    private DailyLogIdentifier dailyLogIdentifier;



    private MealModel breakfastMeal;
    private MealModel lunchMeal;
    private MealModel dinnerMeal;
    private List<MealModel> snackMeals;

    private LocalDate logDate;

    private GoalStatus metCaloriesGoal;

    private GoalStatus metWorkoutGoal;

    private GoalStatus metDailyGoals;
}
