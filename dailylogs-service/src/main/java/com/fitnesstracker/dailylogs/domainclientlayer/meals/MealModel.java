package com.fitnesstracker.dailylogs.domainclientlayer.meals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class MealModel {
    private String mealId;
    private String mealName;
    private Integer mealCalorie;
}
