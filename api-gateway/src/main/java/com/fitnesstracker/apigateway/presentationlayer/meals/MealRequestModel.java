package com.fitnesstracker.apigateway.presentationlayer.meals;

import com.fitnesstracker.apigateway.domainclientlayer.meals.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealRequestModel {
    //private String userId;
    private String mealName;
    private Integer calories;
    private LocalDate mealDate;
    private MealType mealType;
}
