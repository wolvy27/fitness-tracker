package com.fitnesstracker.apigateway.presentationlayer.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel {

    String userId;
    String firstName;
    String lastName;
    Integer age;
    Integer heightInCm;
    Integer weightInKg;
    String goalDescription;
    Integer dailyCaloricIntake;
    List<String> workoutDays;
}
