package com.fitnesstracker.apigateway.presentationlayer.meals;

import com.fitnesstracker.apigateway.domainclientlayer.meals.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealResponseModel extends RepresentationModel<MealResponseModel> {
    private String mealId;
    //private String userId;
    private String mealName;
    private Integer calories;
    private LocalDate mealDate;
    private MealType mealType;
}
