package domainclientlayer.meals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class MealModel {
    private String mealId;
    private String mealName;
    private int mealCalories;
}
