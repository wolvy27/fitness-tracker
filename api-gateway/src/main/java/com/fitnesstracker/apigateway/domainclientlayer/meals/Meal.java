package com.fitnesstracker.apigateway.domainclientlayer.meals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="meals")
@Data
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private MealIdentifier mealIdentifier;

    //@Embedded
    //private UserIdentifier userIdentifier;

    @Column(name="meal_name")
    private String mealName;

    @Column(name="calories")
    private Integer calories;

    @Column(name="meal_date")
    private LocalDate mealDate;

    @Column(name="meal_type")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

}
