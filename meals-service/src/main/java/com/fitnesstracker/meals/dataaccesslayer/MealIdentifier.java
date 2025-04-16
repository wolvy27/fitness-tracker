package com.fitnesstracker.meals.dataaccesslayer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class MealIdentifier {

    @Column(name = "meal_id")
    private String mealId;

    public MealIdentifier() {
        this.mealId = UUID.randomUUID().toString();
    }

    public MealIdentifier(String mealId) {
        this.mealId = mealId;
    }
}
