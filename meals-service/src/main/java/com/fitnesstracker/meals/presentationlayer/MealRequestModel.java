package com.fitnesstracker.meals.presentationlayer;

import com.fitnesstracker.meals.dataaccesslayer.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRequestModel {
    //private String userId;
    private String mealName;
    private Integer calories;
    private LocalDate mealDate;
    private MealType mealType;
}
