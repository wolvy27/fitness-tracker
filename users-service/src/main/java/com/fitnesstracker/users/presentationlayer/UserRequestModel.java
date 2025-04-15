package com.fitnesstracker.users.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestModel {

    String firstName;
    String lastName;
    Integer age;
    Integer heightInCm;
    Integer weightInKg;
    String goalDescription;
    Integer dailyCaloricIntake;
    List<String> workoutDays;
}
